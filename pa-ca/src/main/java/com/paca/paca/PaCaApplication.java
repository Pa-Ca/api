package com.paca.paca;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;

@SpringBootApplication
@SecurityScheme(name = "bearerAuth", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT")
@OpenAPIDefinition(info = @Info(title = "Pa'ca - OpenAPI 1.0", version = "1.0.0", description = "Pa'ca Application API Documentation", termsOfService = "http://swagger.io/terms/", contact = @Contact(name = "Amin Arriaga", email = "aminlorenzo.14@gmail.com"), license = @License(name = "Apache 2.0", url = "http://www.apache.org/licenses/LICENSE-2.0.html")))
public class PaCaApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaCaApplication.class, args);
    }
}
