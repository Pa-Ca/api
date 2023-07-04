# Build stage
FROM maven:3.8.5-openjdk-17-slim AS build
COPY pa-ca/pom.xml /app/
COPY pa-ca/src /app/src
RUN mvn -f /app/pom.xml clean dependency:purge-local-repository package -q

# Run stage
FROM openjdk:17-jdk-alpine
WORKDIR /app
EXPOSE 8080
COPY --from=build /app/target/*.jar /app/app.jar
ENTRYPOINT ["/bin/sh", "-c", "java -jar ./app.jar &> ./output.log || sleep 10000"]