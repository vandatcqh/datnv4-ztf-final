FROM eclipse-temurin:8-jdk-jammy

WORKDIR /app

COPY build/libs/userservice.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]

