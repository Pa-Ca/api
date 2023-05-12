package com.paca.paca.user;

import java.util.List;
import java.util.UUID;

import com.paca.paca.PacaTest;
import com.paca.paca.user.model.Role;
import com.paca.paca.user.model.User;
import com.paca.paca.statics.UserRole;
import com.paca.paca.user.dto.UserDTO;
import com.paca.paca.auth.dto.LoginRequestDTO;
import com.paca.paca.user.statics.UserStatics;
import com.paca.paca.auth.dto.SignupRequestDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paca.paca.user.repository.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.paca.paca.auth.statics.AuthenticationStatics;
import com.paca.paca.exception.exceptions.ForbiddenException;

import org.junit.Assert;
import org.junit.runner.RunWith;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import junit.framework.TestCase;
import io.github.cdimascio.dotenv.Dotenv;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserIntegrationTest extends PacaTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String adminToken;

    private String clientToken;

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

    @BeforeAll
    public void createAdminUser() throws Exception {
        String email = UUID.randomUUID().toString() + "_test@test.com";
        String password = "12345678";

        userRepository.save(User.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .role(Role.builder()
                        .id((long) UserRole.admin.ordinal())
                        .name(UserRole.admin)
                        .build())
                .verified(false)
                .loggedIn(false)
                .build());

        LoginRequestDTO loginRequest = LoginRequestDTO.builder()
                .email(email)
                .password(password)
                .build();
        MvcResult response = mockMvc
                .perform(post(AuthenticationStatics.Endpoint.AUTH_PATH + AuthenticationStatics.Endpoint.LOGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String responseJson = response.getResponse().getContentAsString();
        JsonNode responseNode = objectMapper.readTree(responseJson);

        this.adminToken = responseNode.get("token").asText();
    }

    @BeforeAll
    public void createClientUser() throws Exception {
        String email = UUID.randomUUID().toString() + "_test@test.com";
        String password = "123456789";

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
                .andReturn();
        String responseJson = response.getResponse().getContentAsString();
        JsonNode responseNode = objectMapper.readTree(responseJson);

        this.clientToken = responseNode.get("token").asText();
    }

    @Test
    public void shouldGetAll() throws Exception {
        MvcResult response = mockMvc.perform(get(UserStatics.Endpoint.PATH + UserStatics.Endpoint.GET_ALL)
                .header("Authorization", "Bearer " + this.adminToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String content = response.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(content);
        JsonNode listNode = jsonNode.get("users");
        List<UserDTO> list = objectMapper.convertValue(
                listNode,
                new TypeReference<List<UserDTO>>() {
                });

        assertEquals(list.size(), 39);
    }

    @Test
    public void getAllExceptions() throws Exception {
        // No token exception
        try {
            mockMvc.perform(get(UserStatics.Endpoint.PATH + UserStatics.Endpoint.GET_ALL)
                    .contentType(MediaType.APPLICATION_JSON));
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof ForbiddenException);
            Assert.assertEquals(e.getMessage(), "Authentication failed");
            Assert.assertEquals(((ForbiddenException) e).getCode(), (Integer) 9);
        }

        // Invalid token
        try {
            mockMvc.perform(get(UserStatics.Endpoint.PATH +
                    UserStatics.Endpoint.GET_ALL)
                    .header("Authorization", "Bearer a")
                    .contentType(MediaType.APPLICATION_JSON));
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof ForbiddenException);
            Assert.assertEquals(e.getMessage(), "Authentication failed");
            Assert.assertEquals(((ForbiddenException) e).getCode(), (Integer) 9);
        }

        // No admin user exception
        mockMvc.perform(get(UserStatics.Endpoint.PATH + UserStatics.Endpoint.GET_ALL)
                .header("Authorization", "Bearer " + this.clientToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
    }

    @Test
    public void should_GetByID_Update_And_Delete() throws Exception {
        // Create user
        String email = UUID.randomUUID().toString() + "_test@test.com";
        String password = "123456789";
        SignupRequestDTO signupRequest = SignupRequestDTO.builder()
                .email(email)
                .password(password)
                .role("business")
                .build();
        MvcResult response = mockMvc
                .perform(post(AuthenticationStatics.Endpoint.AUTH_PATH + AuthenticationStatics.Endpoint.SIGNUP)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String responseJson = response.getResponse().getContentAsString();
        JsonNode responseNode = objectMapper.readTree(responseJson);
        Integer id = Integer.parseInt(responseNode.get("id").asText());
        String token = responseNode.get("token").asText();

        // Get user by id
        mockMvc.perform(get((UserStatics.Endpoint.PATH + UserStatics.Endpoint.GET_BY_ID).replace("{id}",
                id.toString()))
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(id)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(email)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.role", CoreMatchers.is("business")));

        // Update user
        String fakeEmail = UUID.randomUUID().toString() + "_test@fake.com";
        UserDTO dto = UserDTO.builder()
                .id(1L)
                .email(fakeEmail)
                .verified(true)
                .loggedIn(true)
                .role("admin")
                .build();
        mockMvc.perform(patch((UserStatics.Endpoint.PATH + UserStatics.Endpoint.UPDATE).replace("{id}",
                id.toString()))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(id)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(email)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.verified", CoreMatchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.loggedIn", CoreMatchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.role", CoreMatchers.is("business")));

        // Delete user
        mockMvc.perform(delete((UserStatics.Endpoint.PATH + UserStatics.Endpoint.DELETE).replace("{id}",
                id.toString()))
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

        // Cannot get user by id
        mockMvc.perform(get((UserStatics.Endpoint.PATH + UserStatics.Endpoint.GET_BY_ID).replace("{id}",
                id.toString()))
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("User with id " + id + " does not exists")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(12)));
    }

    @Test
    public void getByIdExceptions() throws Exception {
        // No token exception
        try {
            mockMvc.perform(
                    get((UserStatics.Endpoint.PATH + UserStatics.Endpoint.GET_BY_ID).replace("{id}", "1"))
                            .contentType(MediaType.APPLICATION_JSON));
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof ForbiddenException);
            Assert.assertEquals(e.getMessage(), "Authentication failed");
            Assert.assertEquals(((ForbiddenException) e).getCode(), (Integer) 9);
        }

        // Invalid token
        try {
            mockMvc.perform(
                    get((UserStatics.Endpoint.PATH + UserStatics.Endpoint.GET_BY_ID).replace("{id}", "1"))
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer a"));
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof ForbiddenException);
            Assert.assertEquals(e.getMessage(), "Authentication failed");
            Assert.assertEquals(((ForbiddenException) e).getCode(), (Integer) 9);
        }

        // User don't exists
        mockMvc.perform(get((UserStatics.Endpoint.PATH + UserStatics.Endpoint.GET_BY_ID).replace("{id}", "0"))
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(12)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("User with id 0 does not exists")));
    }

    @Test
    public void updateExceptions() throws Exception {
        // No token exception
        try {
            mockMvc.perform(
                    patch((UserStatics.Endpoint.PATH + UserStatics.Endpoint.UPDATE).replace("{id}", "1"))
                            .contentType(MediaType.APPLICATION_JSON));
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof ForbiddenException);
            Assert.assertEquals(e.getMessage(), "Authentication failed");
            Assert.assertEquals(((ForbiddenException) e).getCode(), (Integer) 9);
        }

        // Invalid token
        try {
            mockMvc.perform(
                    patch((UserStatics.Endpoint.PATH + UserStatics.Endpoint.UPDATE).replace("{id}", "1"))
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer a"));
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof ForbiddenException);
            Assert.assertEquals(e.getMessage(), "Authentication failed");
            Assert.assertEquals(((ForbiddenException) e).getCode(), (Integer) 9);
        }

        String email = UUID.randomUUID().toString() + "_test@test.com";
        UserDTO dto = UserDTO.builder()
                .id(1L)
                .email(email)
                .verified(true)
                .loggedIn(true)
                .role("client")
                .build();

        // Token don't match with the user to edit
        mockMvc.perform(patch((UserStatics.Endpoint.PATH + UserStatics.Endpoint.UPDATE).replace("{id}", "0"))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + clientToken)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
    }

    @Test
    public void deleteExceptions() throws Exception {
        // No token exception
        try {
            mockMvc.perform(
                    put((UserStatics.Endpoint.PATH + UserStatics.Endpoint.DELETE).replace("{id}", "1"))
                            .contentType(MediaType.APPLICATION_JSON));
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof ForbiddenException);
            Assert.assertEquals(e.getMessage(), "Authentication failed");
            Assert.assertEquals(((ForbiddenException) e).getCode(), (Integer) 9);
        }

        // Invalid token
        try {
            mockMvc.perform(
                    put((UserStatics.Endpoint.PATH + UserStatics.Endpoint.DELETE).replace("{id}", "1"))
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer a"));
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof ForbiddenException);
            Assert.assertEquals(e.getMessage(), "Authentication failed");
            Assert.assertEquals(((ForbiddenException) e).getCode(), (Integer) 9);
        }

        // Token dont match with business to edit
        mockMvc.perform(patch((UserStatics.Endpoint.PATH + UserStatics.Endpoint.DELETE).replace("{id}", "0"))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + clientToken))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
    }
}
