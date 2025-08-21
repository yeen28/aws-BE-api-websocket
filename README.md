# Nameless Social Service

#### 기간: 2025.07.28.~2025.08.18. (약 3주)

#### URL : https://www.teamnameless.click

---

## 개요

실시간 채팅 및 개인 맞춤 퀘스트 제공 등 소셜 기능을 제공하는 서비스입니다.  
API와 WebSocket을 통해 클라이언트와 통신하며, 그룹, 클럽, 채팅, 개인화된 퀘스트 제공으로 소셜 활동을 지원합니다.

---

## 모듈 구조

본 프로젝트는 MSA(Microservice Architecture)를 지향하며, 기능별로 모듈이 분리되어 있습니다.

> chat-api와 chat-websocket은 현재는 모듈로 분리되어 있지만 조만간 각각의 Repository로 분리해서 관리할 목적으로 개발되었습니다.  
> 추후 2개의 Repository로 분리할 예정입니다.

- **`chat-api`**: API 서버로, 사용자인증, 그룹 관리, 퀘스트 관리 등 핵심 비즈니스 로직을 처리합니다.
- **`chat-websocket`**: WebSocket 서버로, 실시간 채팅 및 이벤트 push 기능을 담당합니다.
- **`chat-core`**: 공통으로 사용되는 데이터 모델(Entity) 및 유틸리티 클래스를 포함하는 모듈입니다.

---

## ⚙️ 기술 스택

- Java 17, Spring Boot 3.x, JPA, JWT <!-- WebFlux -->
- **Database**
  - **API**: MySQL
  - **Websocket**: AWS DynamoDB
- **Database Migration**: Liquibase
- **Authentication**: AWS Cognito
- **Build Tool**: Gradle
- **Containerization**: Docker, AWS ECR
- **CI/CD**: Jenkins, Github Action, ArgoCD

## 📋 요구사항

- Java 17 (Amazon Corretto 17 권장)
- Spring Boot 3.x
- MySQL & AWS DynamoDB
- AWS 계정 (Cognito, DynamoDB 사용을 위해)

---

## ERD

![image](readme-image/erd.png)

---

## 🛠️ 환경 설정

프로젝트를 실행하기 위한 설정 방법입니다.

### 1. application.yaml 설정

`chat-api`와 `chat-websocket` 모듈의 `src/main/resources/application.yaml` 파일을 열어 아래 항목들을 자신의 환경에 맞게 수정해야 합니다.

```yaml
# chat-api/src/main/resources/application.yaml 예시

spring:
  datasource:
    url: ${SPRING_DATASOURCE_URL}
    username: ${SPRING_DATASOURCE_USERNAME}
    password: ${SPRING_DATASOURCE_PASSWORD}
    driver-class-name: com.mysql.cj.jdbc.Driver

  jpa:
    hibernate:
      ddl-auto: none # Liquibase를 사용하므로 none으로 설정
    defer-datasource-initialization: true

# AWS Cognito 설정
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: ${ISSUER_URI}
          jwk-set-uri: ${JWK_SET_URI}
        client-id: ${CLIENT_ID} # User Pool에 등록된 앱 클라이언트 ID
```

<!--
```bash
 AWS Cognito 설정
  cloud:
    aws:
      credentials:
        access-key: {AWS_ACCESS_KEY}
        secret-key: {AWS_SECRET_KEY}
      cognito:
        user-pool-id: {COGNITO_USER_POOL_ID}
        region: {AWS_REGION}
        client-id: {COGNITO_CLIENT_ID}
```

- **AWS Cognito 관련 설정**:
  - `{AWS_ACCESS_KEY}`, `{AWS_SECRET_KEY}`: AWS 서비스에 접근하기 위한 IAM 사용자의 Access Key
  - `{COGNITO_USER_POOL_ID}`: AWS Cognito User Pool ID
  - `{AWS_REGION}`: AWS 리전 (예: `ap-northeast-2`)
  - `{COGNITO_CLIENT_ID}`: User Pool에 등록된 앱 클라이언트 ID
-->

### AWS Cognito 설정

- AWS Cognito 서비스에서 사용자 풀(User Pool)을 생성해야 합니다.
- 생성된 User Pool의 ID와 앱 클라이언트 ID를 `application.yaml` 파일에 기입합니다.
<!--
- 원활한 토큰 검증을 위해 Cognito User Pool의 JWKS(JSON Web Key Set) URI가 공개적으로 접근 가능해야 합니다.
-->

---

### 프로젝트 빌드

프로젝트 루트 디렉토리에서 아래 명령어를 실행하여 전체 프로젝트를 빌드합니다.

```bash
./gradlew clean build
```

### Docker로 빌드

프로젝트 루트 디렉토리에 있는 `Dockerfile.api`와 `Dockerfile.websocket` 파일을 사용하여 API 서버와 WebSocket 서버를 Docker build 합니다.

```bash
docker build -f Dockerfile.api -t nameless-api .
docker build -f Dockerfile.websocket -t nameless-ws .
```

<!-- 
```bash
docker-compose up -d --build
```

`-d`: 백그라운드에서 실행
`--build`: 이미지를 새로 빌드하여 실행

서버를 중지하려면 아래 명령어를 사용하세요.
```bash
docker-compose down
```                 
-->

> 본 프로젝트는 Jenkins를 통해 master branch에 commit하면 AWS ECR에 docker image를 push 한 뒤, AWS EKS 환경에서 실행하도록 구성했습니다.

---

## 🗄️ 데이터베이스

- 데이터베이스 스키마 관리는 **Liquibase**를 사용합니다.
- `chat-api/src/main/resources/db/changelog` 디렉토리의 `db.changelog-master.yaml` 파일에 변경사항(Changeset)을 추가하여 스키마를 변경 및 관리합니다.
- 애플리케이션 실행 시 Liquibase가 자동으로 변경사항을 감지하여 데이터베이스에 반영합니다.

## 🔄 CI/CD

- **GitHub Actions**: `.github/workflows/ci-build-test.yml`에 정의되어 있으며, Pull Request 시 자동으로 빌드 및 테스트를 수행합니다.
- **Jenkins**: `jenkinsfile`을 통해 ECR 빌드 및 배포 파이프라인을 구성했습니다.

---

## 기술 문서 & 트러블슈팅
[WIKI](https://github.com/yeen28/aws-backend-api-ws/wiki)

---

## 🖼️ 데모 화면

<!-- 데모 화면 이미지를 여기에 추가하세요. 예: <img src="readme-image/demo1.png" width="400"/> -->

> 전체 화면을 캡쳐하다보니 우측 하단의 챗봇 아이콘이 여러 번 보이는 것입니다.  
> 실제 서비스에서는 페이지를 스크롤해도 우측 하단에 잘 위치해 있습니다.

### 1. 로그인 & 회원가입

![image](readme-image/login.png)

![image](readme-image/sign-up.png)

![image](readme-image/sign-up-cert.png)

### 2. 메인 화면

![image](readme-image/home.png)

![image](readme-image/home-calendar.png)

![image](readme-image/club-search.png)

![image](readme-image/chatbot.png)

### 3. Group 가입 화면

![image](readme-image/group-join.png)

### 4. Club 가입 화면

![image](readme-image/club-join.png)

![image](readme-image/group-club-join-success.png)

### 5. 퀘스트 진행 화면
매일 사용자 맞춤 퀘스트가 3개 할당됩니다.

![image](readme-image/group.png)

![image](readme-image/quest-complete.png)

퀘스트를 완료할 때 퀘스트에 대한 피드백을 전송하면, **AI는 다음 퀘스트 생성할 때 피드백 결과에 따라 개인화된 퀘스트를 생성합니다.**

![image](readme-image/send-feedback.png)

### 6. Club 채팅 화면

![image](readme-image/chat.png)

### 7. 대시보드 화면

![image](readme-image/dashboard.png)

### 8. My Page(profile 설정, group 관리)

#### Profile 설정

![image](readme-image/mypage-profile.png)

#### Group 관리

![image](readme-image/mypage-group.png)

#### 고객 지원

![image](readme-image/mypage-support.png)

#### 계정 관리

![image](readme-image/mypage-account.png)

![image](readme-image/mypage-account-change-password.png)

![image](readme-image/mypage-account-delete.png)

### 9. 기부 화면

![image](readme-image/donation.png)
