package com.paca.paca.auth;

import java.util.UUID;

import com.paca.paca.PacaTest;
import com.paca.paca.auth.dto.LogoutDTO;
import com.paca.paca.auth.dto.LoginRequestDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.paca.paca.auth.dto.SignupRequestDTO;
import com.paca.paca.auth.dto.VerifyEmailRequestDTO;
import com.paca.paca.auth.dto.RefreshRequestDTO;
import com.paca.paca.auth.dto.ResetPasswordDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paca.paca.auth.dto.ResetPasswordRequestDTO;
import com.paca.paca.auth.statics.AuthenticationStatics;

import org.junit.runner.RunWith;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
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
    public void shouldSignupLoginRefreshAndLogout() throws Exception {
        String email = UUID.randomUUID().toString() + "_test@test.com";
        String password = "123456789";

        // Test signup
        SignupRequestDTO signupRequest = SignupRequestDTO.builder()
                .email(email)
                .password(password)
                .role("client")
                .build();
        MvcResult response = mockMvc
                .perform(post(AuthenticationStatics.Endpoint.AUTH_PATH + AuthenticationStatics.Endpoint.SIGNUP)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.role", CoreMatchers.is("client")))
                .andReturn();
        String responseJson = response.getResponse().getContentAsString();
        JsonNode responseNode = objectMapper.readTree(responseJson);
        Integer id = Integer.parseInt(responseNode.get("id").asText());

        // Login same user
        LoginRequestDTO loginRequest = LoginRequestDTO.builder()
                .email(email)
                .password(password)
                .build();
        response = mockMvc
                .perform(post(AuthenticationStatics.Endpoint.AUTH_PATH + AuthenticationStatics.Endpoint.LOGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.role", CoreMatchers.is("client")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(id)))
                .andReturn();
        responseJson = response.getResponse().getContentAsString();
        responseNode = objectMapper.readTree(responseJson);
        String refresh = responseNode.get("refresh").asText();

        // Refresh token
        RefreshRequestDTO refreshRequest = RefreshRequestDTO.builder()
                .refresh(refresh)
                .build();
        response = mockMvc
                .perform(post(AuthenticationStatics.Endpoint.AUTH_PATH + AuthenticationStatics.Endpoint.REFRESH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(refreshRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        responseJson = response.getResponse().getContentAsString();
        responseNode = objectMapper.readTree(responseJson);
        String token = responseNode.get("token").asText();

        // Logout
        LogoutDTO logoutRequest = LogoutDTO.builder()
                .token(token)
                .refresh(refresh)
                .build();
        mockMvc.perform(post(AuthenticationStatics.Endpoint.AUTH_PATH + AuthenticationStatics.Endpoint.LOGOUT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(logoutRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        // Verify that you cannot refresh with the same token after logout
        mockMvc.perform(post(AuthenticationStatics.Endpoint.AUTH_PATH + AuthenticationStatics.Endpoint.REFRESH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(refreshRequest)))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(9)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Authentication failed")));
    }

    @Test
    public void signupExceptions() throws Exception {
        // Exception for invalid email
        SignupRequestDTO signupRequest = SignupRequestDTO.builder()
                .email("A")
                .password("123456789")
                .role("client")
                .build();
        mockMvc.perform(post(AuthenticationStatics.Endpoint.AUTH_PATH + AuthenticationStatics.Endpoint.SIGNUP)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Invalid email format")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)));

        // Create a valid user
        String email = UUID.randomUUID().toString() + "_test@test.com";
        String password = "123456789";
        signupRequest = SignupRequestDTO.builder()
                .email(email)
                .password(password)
                .role("client")
                .build();
        mockMvc.perform(post(AuthenticationStatics.Endpoint.AUTH_PATH + AuthenticationStatics.Endpoint.SIGNUP)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        // Exception for repeated mail
        signupRequest = SignupRequestDTO.builder()
                .email(email)
                .password(password)
                .role("client")
                .build();
        mockMvc.perform(post(AuthenticationStatics.Endpoint.AUTH_PATH + AuthenticationStatics.Endpoint.SIGNUP)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("User already exists")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(1)));

        // Exception for invalid password
        signupRequest = SignupRequestDTO.builder()
                .email(UUID.randomUUID().toString() + "_test@test.com")
                .password("A")
                .role("client")
                .build();
        mockMvc.perform(post(AuthenticationStatics.Endpoint.AUTH_PATH + AuthenticationStatics.Endpoint.SIGNUP)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Invalid password")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(2)));

        // Exception for invalid role
        signupRequest = SignupRequestDTO.builder()
                .email(UUID.randomUUID().toString() + "_test@test.com")
                .password("123456789")
                .role("bad_role")
                .build();
        mockMvc.perform(post(AuthenticationStatics.Endpoint.AUTH_PATH + AuthenticationStatics.Endpoint.SIGNUP)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("The role given is not valid")));
    }

    @Test
    public void loginExceptions() throws Exception {
        String email = UUID.randomUUID().toString() + "_test@test.com";
        String password = "123456789";

        // Exception for invalid email
        LoginRequestDTO loginRequest = LoginRequestDTO.builder()
                .email(email)
                .password(password)
                .build();
        mockMvc.perform(post(AuthenticationStatics.Endpoint.AUTH_PATH + AuthenticationStatics.Endpoint.LOGIN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Authentication failed")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(9)));

        // Create a valid user
        SignupRequestDTO signupRequest = SignupRequestDTO.builder()
                .email(email)
                .password(password)
                .role("client")
                .build();
        mockMvc.perform(post(AuthenticationStatics.Endpoint.AUTH_PATH + AuthenticationStatics.Endpoint.SIGNUP)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        // Exception for invalid password
        loginRequest = LoginRequestDTO.builder()
                .email(email)
                .password(password + ".")
                .build();
        mockMvc.perform(post(AuthenticationStatics.Endpoint.AUTH_PATH + AuthenticationStatics.Endpoint.LOGIN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Authentication failed")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(9)));
    }

    @Test
    public void refreshExceptions() throws Exception {
        String email = UUID.randomUUID().toString() + "_test@test.com";
        String password = "123456789";

        // Create a valid user
        SignupRequestDTO signupRequest = SignupRequestDTO.builder()
                .email(email)
                .password(password)
                .role("client")
                .build();
        MvcResult response = mockMvc
                .perform(post(AuthenticationStatics.Endpoint.AUTH_PATH + AuthenticationStatics.Endpoint.SIGNUP)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.role", CoreMatchers.is("client")))
                .andReturn();
        String responseJson = response.getResponse().getContentAsString();
        JsonNode responseNode = objectMapper.readTree(responseJson);
        String token = responseNode.get("id").asText();
        String refresh = responseNode.get("id").asText();

        // Exception for invalid token
        RefreshRequestDTO refreshRequest = RefreshRequestDTO.builder()
                .refresh(refresh + ".")
                .build();
        mockMvc.perform(post(AuthenticationStatics.Endpoint.AUTH_PATH + AuthenticationStatics.Endpoint.REFRESH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(refreshRequest)))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Authentication failed")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(9)));

        // Exception for not refresh token
        refreshRequest = RefreshRequestDTO.builder()
                .refresh(token)
                .build();
        mockMvc.perform(post(AuthenticationStatics.Endpoint.AUTH_PATH + AuthenticationStatics.Endpoint.REFRESH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(refreshRequest)))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Authentication failed")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(9)));
    }

    @Test
    public void shouldResetPassword() throws Exception {
        String email = UUID.randomUUID().toString() + "_test@test.com";
        String password = "123456789";

        // Signup
        SignupRequestDTO signupRequest = SignupRequestDTO.builder()
                .email(email)
                .password(password)
                .role("client")
                .build();
        MvcResult response = mockMvc
                .perform(post(AuthenticationStatics.Endpoint.AUTH_PATH + AuthenticationStatics.Endpoint.SIGNUP)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.role", CoreMatchers.is("client")))
                .andReturn();
        String responseJson = response.getResponse().getContentAsString();
        JsonNode responseNode = objectMapper.readTree(responseJson);
        Integer id = Integer.parseInt(responseNode.get("id").asText());

        // Send reset password request
        ResetPasswordRequestDTO resetPasswordRequest = ResetPasswordRequestDTO.builder()
                .email(email)
                .build();
        response = mockMvc
                .perform(post(AuthenticationStatics.Endpoint.AUTH_PATH
                        + AuthenticationStatics.Endpoint.RESET_PASSWORD_REQUEST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resetPasswordRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        responseJson = response.getResponse().getContentAsString();
        responseNode = objectMapper.readTree(responseJson);
        String token = responseNode.get("token").asText();

        // Change password
        String newPassword = "password098";
        ResetPasswordDTO resetPassword = ResetPasswordDTO.builder()
                .password(newPassword)
                .build();
        mockMvc.perform(post(AuthenticationStatics.Endpoint.AUTH_PATH
                + AuthenticationStatics.Endpoint.RESET_PASSWORD
                + "/" + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(resetPassword)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        // Verify that you cannot login with the old password
        LoginRequestDTO loginRequest = LoginRequestDTO.builder()
                .email(email)
                .password(password)
                .build();
        mockMvc.perform(post(AuthenticationStatics.Endpoint.AUTH_PATH + AuthenticationStatics.Endpoint.LOGIN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(9)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Authentication failed")));

        // Verify that you can login with the new password
        loginRequest = LoginRequestDTO.builder()
                .email(email)
                .password(newPassword)
                .build();
        mockMvc.perform(post(AuthenticationStatics.Endpoint.AUTH_PATH + AuthenticationStatics.Endpoint.LOGIN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.role", CoreMatchers.is("client")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(id)));
    }

    @Test
    public void resetPasswordRequestExceptions() throws Exception {
        String email = UUID.randomUUID().toString() + "_test@test.com";

        // Exception for invalid email
        ResetPasswordRequestDTO resetPasswordRequest = ResetPasswordRequestDTO.builder()
                .email(email)
                .build();
        mockMvc.perform(post(AuthenticationStatics.Endpoint.AUTH_PATH
                + AuthenticationStatics.Endpoint.RESET_PASSWORD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(resetPasswordRequest)))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("User with email " + email + " does not exists")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(30)));
    }

    @Test
    public void resetPasswordExceptions() throws Exception {
        String email = UUID.randomUUID().toString() + "_test@test.com";
        String password = "123456789";

        // Create a valid user
        SignupRequestDTO signupRequest = SignupRequestDTO.builder()
                .email(email)
                .password(password)
                .role("client")
                .build();
        mockMvc.perform(post(AuthenticationStatics.Endpoint.AUTH_PATH + AuthenticationStatics.Endpoint.SIGNUP)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.role", CoreMatchers.is("client")));

        // Send reset password request
        ResetPasswordRequestDTO resetPasswordRequest = ResetPasswordRequestDTO.builder()
                .email(email)
                .build();
        MvcResult response = mockMvc
                .perform(post(AuthenticationStatics.Endpoint.AUTH_PATH
                        + AuthenticationStatics.Endpoint.RESET_PASSWORD_REQUEST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resetPasswordRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String responseJson = response.getResponse().getContentAsString();
        JsonNode responseNode = objectMapper.readTree(responseJson);
        String token = responseNode.get("token").asText();

        // Exception for invalid token
        ResetPasswordDTO resetPassword = ResetPasswordDTO.builder()
                .password(password)
                .build();
        mockMvc.perform(post(AuthenticationStatics.Endpoint.AUTH_PATH
                + AuthenticationStatics.Endpoint.RESET_PASSWORD
                + "/" + token + "a")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(resetPassword)))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Authentication failed")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(9)));

        // Exception for invalid password
        resetPassword = ResetPasswordDTO.builder()
                .password("a")
                .build();
        mockMvc.perform(post(AuthenticationStatics.Endpoint.AUTH_PATH
                + AuthenticationStatics.Endpoint.RESET_PASSWORD
                + "/" + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(resetPassword)))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Invalid password")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(2)));
    }

    @Test
    public void shouldVerifyEmail() throws Exception {
        String email = UUID.randomUUID().toString() + "_test@test.com";
        String password = "123456789";

        // Signup
        SignupRequestDTO signupRequest = SignupRequestDTO.builder()
                .email(email)
                .password(password)
                .role("client")
                .build();
        MvcResult response = mockMvc
                .perform(post(AuthenticationStatics.Endpoint.AUTH_PATH + AuthenticationStatics.Endpoint.SIGNUP)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.role", CoreMatchers.is("client")))
                .andReturn();
        String responseJson = response.getResponse().getContentAsString();
        JsonNode responseNode = objectMapper.readTree(responseJson);

        // Send verify email request
        VerifyEmailRequestDTO verifyEmailRequest = VerifyEmailRequestDTO.builder()
                .email(email)
                .build();
        response = mockMvc
                .perform(post(AuthenticationStatics.Endpoint.AUTH_PATH
                        + AuthenticationStatics.Endpoint.VERIFY_EMAIL_REQUEST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(verifyEmailRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        responseJson = response.getResponse().getContentAsString();
        responseNode = objectMapper.readTree(responseJson);
        String token = responseNode.get("token").asText();

        // Verify email
        mockMvc.perform(post(AuthenticationStatics.Endpoint.AUTH_PATH
                + AuthenticationStatics.Endpoint.VERIFY_EMAIL
                + "/" + token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void verifyEmailRequestExceptions() throws Exception {
        String email = UUID.randomUUID().toString() + "_test@test.com";

        // Exception for invalid email
        VerifyEmailRequestDTO verifyEmailRequest = VerifyEmailRequestDTO.builder()
                .email(email)
                .build();
        mockMvc.perform(post(AuthenticationStatics.Endpoint.AUTH_PATH
                + AuthenticationStatics.Endpoint.VERIFY_EMAIL_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(verifyEmailRequest)))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("User with email " + email + " does not exists")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(30)));
    }

    @Test
    public void verifyEmailExceptions() throws Exception {
        // Exception for invalid token
        mockMvc.perform(post(AuthenticationStatics.Endpoint.AUTH_PATH
                + AuthenticationStatics.Endpoint.VERIFY_EMAIL
                + "/a")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Authentication failed")));

    }

}
