FROM openjdk:17-alpine

# Set the working directory
WORKDIR /app

RUN cd pa-ca && ./mvnw package && cd ..
ARG JAR_FILE=pa-ca/target/*.jar
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar", "./app.jar"]