FROM amazoncorretto:17-alpine-jre
WORKDIR /app
COPY chat-consumer/build/libs/*-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
