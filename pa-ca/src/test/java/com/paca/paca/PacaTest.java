package com.paca.paca;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

public abstract class PacaTest {
    public static final PostgreSQLContainer POSTGRES_SQL_CONTAINER;
    static {
       POSTGRES_SQL_CONTAINER = new  PostgreSQLContainer("postgres:15.1");
       POSTGRES_SQL_CONTAINER.start();
    }

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES_SQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.password", POSTGRES_SQL_CONTAINER::getPassword);
        registry.add("spring.datasource.username", POSTGRES_SQL_CONTAINER::getUsername);
    }
}
