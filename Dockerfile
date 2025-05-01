FROM openjdk:21-jdk-slim

ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

EXPOSE 8088

ENTRYPOINT ["java","-jar","/app.jar"]
