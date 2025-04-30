FROM amazoncorretto:11-alpine-jdk
COPY target/*.jar app.jar
COPY wait-for-it.sh ./wait-for-it.sh
ENTRYPOINT ["java","-jar","/app.jar"]