# OpenJDK 이미지를 기반으로 Spring Boot 애플리케이션을 실행
FROM openjdk:17-jdk-slim as build

WORKDIR /app

# Spring Boot 애플리케이션 JAR 파일을 복사
COPY /build/libs/*.jar app.jar

# 8080 포트로 서비스
EXPOSE 8090

CMD ["java", "-jar", "/app/app.jar"]
