# Используем официальный образ Amazon Corretto с Java 21
FROM amazoncorretto:21-alpine3.18-jdk

# Копируем собранный JAR-файл в контейнер
COPY target/*.jar app.jar

# Копируем скрипт wait-for-it.sh в корень контейнера
COPY wait-for-it.sh /wait-for-it.sh

# Даем права на выполнение скрипта
RUN chmod +x /wait-for-it.sh

# Запуск приложения после проверки доступности stats-server
ENTRYPOINT ["/wait-for-it.sh", "stats-server:9090", "--timeout=180", "--strict", "--", "java", "-jar", "app.jar"]