FROM gradle:8.10.2-jdk17 AS build

WORKDIR /app

# docker build --build-arg AWS_ACCESS_KEY_ID=... \
# --build-arg AWS_SECRET_ACCESS_KEY=... \
# -t <tag> .

ARG AWS_ACCESS_KEY_ID
ARG AWS_SECRET_ACCESS_KEY

# Pod 실행 시에 /root/.aws에 secret mount하여 SSM 권한 허용 필요

RUN mkdir -p /root/.aws

RUN echo "[default]" > /root/.aws/credentials && \
    echo "aws_access_key_id = ${AWS_ACCESS_KEY_ID}" >> /root/.aws/credentials && \
    echo "aws_secret_access_key = ${AWS_SECRET_ACCESS_KEY}" >> /root/.aws/credentials

RUN echo "[default]" > /root/.aws/config && \
    echo "region = ap-northeast-2" >> /root/.aws/config

COPY gradlew *.gradle ./
COPY gradle ./gradle

COPY . .

RUN ./gradlew test

# 이미지에 민감정보 제외
RUN rm -rf /root/.aws

RUN ./gradlew assemble


FROM openjdk:17-slim

WORKDIR /app

COPY --from=build /app/build/libs/joing-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]