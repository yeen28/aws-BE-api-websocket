# websocket

- Java 17
- SpringBoot 3.3.2
- Gradle 8.4

---
## DB 설정

> https://github.com/aws-devlopment-project/backend-api-websocket/blob/master/chat-api/src/main/resources/application.yaml

```yaml
spring:
  datasource:
    url: ${SPRING_DATASOURCE_URL}
    username: ${SPRING_DATASOURCE_USERNAME:root}
    password: ${SPRING_DATASOURCE_PASSWORD}
    driver-class-name: com.mysql.cj.jdbc.Driver
```

## 사용자 인증(Cognito)을 위한 설정

> https://github.com/aws-devlopment-project/backend-api-websocket/blob/master/chat-api/src/main/resources/application.yaml

```yaml
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: ${ISSUER_URI:input_uri}
          jwk-set-uri: ${JWK_SET_URI:input_uri}
        client-id: ${JWK_SET_URI:input_client_id}
```

---

Open API 문서
- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI JSON 스펙: http://localhost:8080/api-docs

---

### [tree](tree)
