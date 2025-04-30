FROM amazoncorretto:21-alpine-jdk  # Используем Java 21
ENV JAVA_TOOL_OPTIONS -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:9091
COPY stat-service/server/target/*.jar app.jar  # Правильный путь к JAR
ENTRYPOINT ["java", "-jar", "/app.jar"]