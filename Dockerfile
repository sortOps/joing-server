FROM gradle:8.10.2-jdk17 AS build

WORKDIR /app

COPY gradlew build.gradle settings.gradle ./
COPY gradle ./gradle

COPY . .

RUN ./gradlew clean build


FROM openjdk:17-slim

WORKDIR /app

COPY --from=build /app/build/libs/joing-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]