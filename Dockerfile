FROM openjdk:17-jdk-slim-buster

WORKDIR /app

COPY build/libs/api-server-0.0.1-SNAPSHOT.jar app.jar

CMD ["java", "-jar", "app.jar"]