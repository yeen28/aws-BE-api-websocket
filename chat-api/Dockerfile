# Gradle 빌드 이미지를 사용해 애플리케이션을 빌드
FROM gradle:8.4-jdk17-alpine AS build
WORKDIR /app
COPY . .
RUN gradle clean build --no-daemon

FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY build/libs/*-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]