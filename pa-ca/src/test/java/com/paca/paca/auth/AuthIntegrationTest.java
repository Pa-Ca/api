package com.paca.paca.auth;

import java.util.UUID;

import com.paca.paca.PacaTest;
import com.paca.paca.auth.dto.SignupRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paca.paca.auth.statics.AuthenticationStatics;

import org.junit.runner.RunWith;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class AuthIntegrationTest extends PacaTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    static {
        Dotenv dotenv = Dotenv.load();

        System.setProperty("auth.secret.key", dotenv.get("AUTH_SECRET_KEY"));
        System.setProperty("auth.expiration.token", dotenv.get("AUTH_TOKEN_EXPIRATION"));
        System.setProperty("auth.expiration.refresh", dotenv.get("AUTH_REFRESH_EXPIRATION"));
        System.setProperty("auth.expiration.verify.email", dotenv.get("AUTH_VERIFY_EMAIL_EXPIRATION"));
        System.setProperty("auth.expiration.reset.password", dotenv.get("AUTH_RESET_PASSWORD_EXPIRATION"));

        System.setProperty("spring.mail.username", dotenv.get("GOOGLE_EMAIL_FROM"));
        System.setProperty("spring.mail.password", dotenv.get("GOOGLE_EMAIL_PASSWORD"));
    }

    @Test
    public void testSignup() throws Exception {
        String email = UUID.randomUUID().toString() + "_test@test.com";
        String password = "123456789";

        // Test signup
        SignupRequestDTO request = SignupRequestDTO.builder()
                .email(email)
                .password(password)
                .role("client")
                .build();
        mockMvc.perform(post(AuthenticationStatics.Endpoint.AUTH_PATH + AuthenticationStatics.Endpoint.SIGNUP)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.role", CoreMatchers.is("client")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(1015)));
    }
}
