# Build stage
FROM maven:3.8.5-openjdk-17-slim AS build
COPY pa-ca/pom.xml /app/
COPY pa-ca/src /app/src
RUN mvn -f /app/pom.xml clean dependency:purge-local-repository package -Dmaven.test.skip -q

# Run stage
FROM openjdk:17-jdk-alpine
WORKDIR /app
EXPOSE 8080
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/paca_db
ENV SPRING_DATASOURCE_USERNAME=postgres
ENV SPRING_DATASOURCE_PASSWORD=postgres
COPY --from=build /app/target/*.jar /app/app.jar
ENTRYPOINT ["/bin/sh", "-c", "java -jar ./app.jar &> ./output.log || sleep 10000"]