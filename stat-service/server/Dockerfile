FROM amazoncorretto:21-alpine-jdk
ENV JAVA_TOOL_OPTIONS -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:9091
# Используйте относительный путь от корня проекта
COPY stat-service/server/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]