FROM amazoncorretto:21-alpine3.18-jdk

RUN apk add --no-cache netcat-openbsd

COPY target/*.jar app.jar
COPY wait-for-it.sh /wait-for-it.sh

RUN chmod +x /wait-for-it.sh

ENTRYPOINT ["sh", "/wait-for-it.sh", "stats-server:9090", "--timeout=5", "--strict", "--", "java", "-jar", "app.jar"]