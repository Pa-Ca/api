FROM openjdk:17-alpine

# Set the working directory
WORKDIR /app

ARG JAR_FILE=pa-ca/target/*.jar
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar", "./app.jar"]