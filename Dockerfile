FROM gradle:8.10.2-jdk17 AS build

WORKDIR /app

ARG AWS_ACCESS_KEY_ID
ARG AWS_SECRET_ACCESS_KEY

COPY gradlew *.gradle ./
COPY gradle ./gradle

COPY . .

RUN ./gradlew clean assemble


FROM openjdk:17.0-slim

WORKDIR /app

COPY --from=build /app/build/libs/joing-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
