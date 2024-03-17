FROM eclipse-temurin:21-jdk-alpine

RUN addgroup -S app && adduser -S app -G app

USER app
WORKDIR /home/app

COPY target/oauth-server.jar oauth-server.jar

CMD ["java", "-jar", "/home/app/oauth-server.jar"]
