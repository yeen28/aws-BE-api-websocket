# AWS WebSocket 채팅 API

이 프로젝트는 AWS API Gateway (WebSocket), Lambda, DynamoDB, Cognito를 사용하는 서버리스 채팅 애플리케이션입니다.

## 아키텍처

```
                               +--------------------------+
                               |      Amazon Cognito      |
                               | (사용자 인증)            |
                               +-------------+------------+
                                             | (JWT 토큰)
                                             |
[클라이언트] <--(WebSocket)--> [API Gateway] ----> [AWS Lambda 함수]
                                     ^                    |           ^
                                     | (메시지 푸시)      |           | (메시지 저장)
                                     |                    v           |
+------------------------------------+----------+  +-----------------+  |
| AWS Lambda (스트림으로부터 팬아웃) <----[Stream]<--|  Amazon DynamoDB  |  |
+------------------------------------+          +-----------------+  |
                                                                     |
                                                                     v
                                                                [sendMessage Lambda]
```

- **API Gateway (WebSocket):** 실시간 양방향 통신을 처리합니다.
- **AWS Lambda:** 연결, 메시징 등 비즈니스 로직을 실행합니다.
- **Amazon DynamoDB:** 채팅 메시지와 연결 정보를 저장합니다.
- **Amazon Cognito:** 사용자 인증을 관리합니다.

## 동작 방식

### WebSocket 라우트

- **`$connect`**: 클라이언트가 WebSocket API에 연결하면 `connect` Lambda 핸들러가 호출됩니다. 이 핸들러는 `connectionId`를 `Connections` DynamoDB 테이블에 저장합니다.
- **`$disconnect`**: 클라이언트 연결이 끊어지면 `disconnect` Lambda 핸들러가 호출됩니다. 이 핸들러는 `Connections` 테이블에서 `connectionId`를 제거합니다.
- **`joinGroup`**: 클라이언트가 이 라우트로 `groupId`를 포함한 메시지를 보냅니다. `joinGroup` Lambda 핸들러는 `Connections` 테이블에서 해당 클라이언트의 항목에 `groupId`를 업데이트합니다.
- **`sendMessage`**: 클라이언트가 이 라우트로 메시지를 보냅니다. `sendMessage` Lambda 핸들러는 `Connections` 테이블에서 클라이언트의 `groupId`를 조회한 후, `groupId`를 포함한 메시지를 `Messages` 테이블에 저장합니다.

### DynamoDB와 팬아웃(Fan-out)

- **`Messages` 테이블 스트림**: `Messages` 테이블에는 DynamoDB 스트림이 활성화되어 있습니다. 새 메시지가 삽입될 때마다 이 스트림은 `fanoutMessage` Lambda 핸들러를 트리거합니다.
- **`fanoutMessage` 핸들러**: 이 핸들러는 스트림에서 새 메시지를 읽고, `Connections` 테이블의 `GroupIdIndex`를 쿼리하여 동일한 `groupId`에 속한 모든 `connectionId`를 찾습니다. 그런 다음 API Gateway 관리 API를 사용하여 해당 그룹의 모든 연결된 클라이언트에게 메시지를 보냅니다.

## DynamoDB 테이블

- **`Connections`**: 연결된 각 클라이언트의 `connectionId`와 참여한 `groupId`를 저장합니다.
  - `connectionId` (파티션 키)
  - `groupId` (쿼리를 위한 인덱스)
- **`Messages`**: 모든 채팅 메시지를 저장합니다.
  - `messageId` (파티션 키)
  - `groupId`
  - `message`
  - `sender`
  - `createdAt`

## 배포

1. **의존성 설치**: `npm install`
2. **배포**: `serverless deploy`

## 테스트

`wscat`과 같은 커맨드라인 도구를 사용하여 WebSocket API를 테스트할 수 있습니다.

1. **`serverless deploy` 출력에서 WebSocket URL을 확인합니다.**
2. **API에 연결합니다**:
   ```bash
   wscat -c wss://<your-websocket-url>
   ```
3. **그룹에 참여합니다**:
   ```json
   { "action": "joinGroup", "groupId": "group1" }
   ```
4. **메시지를 보냅니다**:
   ```json
   { "action": "sendMessage", "message": "안녕하세요!" }
   ```

`group1`에 연결하고 참여한 다른 모든 클라이언트가 이 메시지를 수신하게 됩니다.
