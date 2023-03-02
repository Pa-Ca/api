# Build stage
FROM maven:3.8.5-openjdk-17-slim AS build
COPY pa-ca/pom.xml /app/
COPY pa-ca/src /app/src
RUN mvn -f /app/pom.xml clean package -Dmaven.test.skip

# Run stage
FROM openjdk:17-jdk-alpine
COPY --from=build /app/target/app*.jar /app/app.jar
ENTRYPOINT ["java", "-jar", "./app.jar"]