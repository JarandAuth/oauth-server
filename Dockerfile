FROM maven:3.9.5-eclipse-temurin-21-alpine AS builder

COPY pom.xml .
RUN mvn -B dependency:go-offline

COPY src src
RUN mvn -B package

FROM eclipse-temurin:21-jdk-alpine

RUN addgroup -S app && adduser -S app -G app

USER app
WORKDIR /home/app

COPY --from=builder target/oauth-server.jar oauth-server.jar

CMD ["java", "-jar", "/home/app/oauth-server.jar"]
