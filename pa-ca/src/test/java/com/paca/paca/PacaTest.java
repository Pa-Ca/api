package com.paca.paca;

import io.github.cdimascio.dotenv.Dotenv;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.DynamicPropertyRegistry;

public abstract class PacaTest {
    public static final PostgreSQLContainer<?> POSTGRES_SQL_CONTAINER;

    static {
        Dotenv dotenv = Dotenv.load();

        DockerImageName myImage = DockerImageName.parse("jrbarreram/postgres_extensions:v1.0");
        myImage = myImage.asCompatibleSubstituteFor("postgres");
        POSTGRES_SQL_CONTAINER = new PostgreSQLContainer<>(myImage);
        POSTGRES_SQL_CONTAINER.withDatabaseName(dotenv.get("SPRING_DATASOURCE_DATABASE_NAME"))
        .withUsername(dotenv.get("SPRING_DATASOURCE_USERNAME"))
        .withPassword(dotenv.get("SPRING_DATASOURCE_PASSWORD"))
        .start();

        System.setProperty("auth.secret.key", dotenv.get("AUTH_SECRET_KEY"));
        System.setProperty("auth.expiration.token", dotenv.get("AUTH_TOKEN_EXPIRATION"));
        System.setProperty("auth.expiration.refresh", dotenv.get("AUTH_REFRESH_EXPIRATION"));
        System.setProperty("auth.expiration.verify.email", dotenv.get("AUTH_VERIFY_EMAIL_EXPIRATION"));
        System.setProperty("auth.expiration.reset.password", dotenv.get("AUTH_RESET_PASSWORD_EXPIRATION"));

        System.setProperty("spring.mail.username", dotenv.get("GOOGLE_EMAIL_FROM"));
        System.setProperty("spring.mail.password", dotenv.get("GOOGLE_EMAIL_PASSWORD"));

        System.setProperty("google.client.id", dotenv.get("GOOGLE_CLIENT_ID"));
    }

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES_SQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.password", POSTGRES_SQL_CONTAINER::getPassword);
        registry.add("spring.datasource.username", POSTGRES_SQL_CONTAINER::getUsername);
    }
}