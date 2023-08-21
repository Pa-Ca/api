package com.paca.paca;

import io.github.cdimascio.dotenv.Dotenv;

import org.testcontainers.containers.PostgreSQLContainer;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.DynamicPropertyRegistry;

public abstract class PacaTest {
    public static final PostgreSQLContainer<?> POSTGRES_SQL_CONTAINER;

    static {
        POSTGRES_SQL_CONTAINER = new PostgreSQLContainer<>("postgres:15.1");
        POSTGRES_SQL_CONTAINER.start();

        Dotenv dotenv = Dotenv.load();

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
