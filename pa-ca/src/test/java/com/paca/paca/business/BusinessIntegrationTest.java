package com.paca.paca.business;

import java.util.List;
import java.util.UUID;
import java.time.Duration;
import java.time.LocalTime;
import java.math.BigDecimal;

import com.paca.paca.PacaTest;
import com.paca.paca.user.model.Role;
import com.paca.paca.user.model.User;
import com.paca.paca.statics.UserRole;
import com.paca.paca.branch.dto.BranchDTO;
import com.paca.paca.business.dto.BusinessDTO;
import com.paca.paca.auth.dto.LoginRequestDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.paca.paca.auth.dto.SignupRequestDTO;
import com.paca.paca.branch.statics.BranchStatics;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paca.paca.user.repository.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.paca.paca.business.statics.BusinessStatics;
import com.paca.paca.auth.statics.AuthenticationStatics;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

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

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BusinessIntegrationTest extends PacaTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String adminToken;

    private String businessToken;

    static {
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
                .perform(post(AuthenticationStatics.Endpoint.AUTH_PATH
                        + AuthenticationStatics.Endpoint.LOGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String responseJson = response.getResponse().getContentAsString();
        JsonNode responseNode = objectMapper.readTree(responseJson);

        this.adminToken = responseNode.get("token").asText();
    }

    @BeforeAll
    public void createBusinessUser() throws Exception {
        String email = UUID.randomUUID().toString() + "_test@test.com";
        String password = "123456789";

        SignupRequestDTO signupRequest = SignupRequestDTO.builder()
                .email(email)
                .password(password)
                .role("business")
                .build();
        MvcResult response = mockMvc
                .perform(post(AuthenticationStatics.Endpoint.AUTH_PATH
                        + AuthenticationStatics.Endpoint.SIGNUP)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String responseJson = response.getResponse().getContentAsString();
        JsonNode responseNode = objectMapper.readTree(responseJson);

        this.businessToken = responseNode.get("token").asText();
    }

    @BeforeAll
    public void registerModules() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    public void shouldGetAll() throws Exception {
        MvcResult response = mockMvc
                .perform(get(BusinessStatics.Endpoint.PATH + BusinessStatics.Endpoint.GET_ALL)
                        .header("Authorization", "Bearer " + this.adminToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String content = response.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(content);
        JsonNode listNode = jsonNode.get("business");
        List<BusinessDTO> list = objectMapper.convertValue(
                listNode,
                new TypeReference<List<BusinessDTO>>() {
                });

        assertEquals(list.size(), 10);
    }

    @Test
    public void getAllExceptions() throws Exception {
        // No token exception
        mockMvc.perform(get(BusinessStatics.Endpoint.PATH + BusinessStatics.Endpoint.GET_ALL)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Authentication failed")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code",
                        CoreMatchers.is(9)));

        // Invalid token
        mockMvc.perform(get(BusinessStatics.Endpoint.PATH + BusinessStatics.Endpoint.GET_ALL)
                .header("Authorization", "Bearer a")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Authentication failed")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code",
                        CoreMatchers.is(9)));

        // No admin user exception
        mockMvc.perform(get(BusinessStatics.Endpoint.PATH + BusinessStatics.Endpoint.GET_ALL)
                .header("Authorization", "Bearer " + this.businessToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
    }

    @Test
    public void should_Save_GetByID_GetByUserID_GetAllBranches_Update_And_Delete() throws Exception {
        // Create user
        String email = UUID.randomUUID().toString() + "_test@test.com";
        String password = "123456789";
        SignupRequestDTO signupRequest = SignupRequestDTO.builder()
                .email(email)
                .password(password)
                .role("business")
                .build();
        MvcResult response = mockMvc
                .perform(post(AuthenticationStatics.Endpoint.AUTH_PATH
                        + AuthenticationStatics.Endpoint.SIGNUP)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String responseJson = response.getResponse().getContentAsString();
        JsonNode responseNode = objectMapper.readTree(responseJson);
        Integer userId = Integer.parseInt(responseNode.get("id").asText());
        String token = responseNode.get("token").asText();

        // Create business
        BusinessDTO dto = BusinessDTO.builder()
                .email(email)
                .name("Test name")
                .verified(false)
                .tier("basic")
                .phoneNumber("Test phone")
                .build();
        response = mockMvc.perform(post(BusinessStatics.Endpoint.PATH + BusinessStatics.Endpoint.SAVE)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(email)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is("Test name")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.verified", CoreMatchers.is(false)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.tier", CoreMatchers.is("basic")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber",
                        CoreMatchers.is("Test phone")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId", CoreMatchers.is(userId)))
                .andReturn();
        responseJson = response.getResponse().getContentAsString();
        responseNode = objectMapper.readTree(responseJson);
        Integer id = Integer.parseInt(responseNode.get("id").asText());

        // Get business by id
        mockMvc.perform(get((BusinessStatics.Endpoint.PATH + BusinessStatics.Endpoint.GET_BY_ID).replace("{id}",
                id.toString()))
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(id)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(email)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is("Test name")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.verified", CoreMatchers.is(false)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.tier", CoreMatchers.is("basic")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber",
                        CoreMatchers.is("Test phone")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId", CoreMatchers.is(userId)));

        // Get business by user id
        mockMvc.perform(get((BusinessStatics.Endpoint.PATH + BusinessStatics.Endpoint.GET_BY_USER_ID).replace(
                "{id}",
                userId.toString()))
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(id)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(email)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is("Test name")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.verified", CoreMatchers.is(false)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.tier", CoreMatchers.is("basic")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber",
                        CoreMatchers.is("Test phone")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId", CoreMatchers.is(userId)));

        // Create branches
        BranchDTO branchDTO = BranchDTO.builder()
                .businessId(id.longValue())
                .location("location test")
                .mapsLink("mapsLink test")
                .name("test")
                .overview("overview test")
                .score(1.0F)
                .capacity(1)
                .reservationPrice(BigDecimal.valueOf(1.0F))
                .reserveOff(false)
                .averageReserveTime(Duration.ofHours(2).plusMinutes(45))
                .visibility(true)
                .phoneNumber("test phone")
                .type("test type")
                .hourIn(LocalTime.of(8, 0))
                .hourOut(LocalTime.of(8, 0))
                .build();
        mockMvc.perform(post(BranchStatics.Endpoint.PATH + BranchStatics.Endpoint.SAVE)
                .header("Authorization", "Bearer " + token)
                .content(objectMapper.writeValueAsString(branchDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
        mockMvc.perform(post(BranchStatics.Endpoint.PATH + BranchStatics.Endpoint.SAVE)
                .header("Authorization", "Bearer " + token)
                .content(objectMapper.writeValueAsString(branchDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

        // Get all branches
        response = mockMvc
                .perform(get((BusinessStatics.Endpoint.PATH + BusinessStatics.Endpoint.GET_BRANCHES)
                        .replace("{id}",
                                id.toString()))
                        .header("Authorization", "Bearer " + this.adminToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String content = response.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(content);
        JsonNode listNode = jsonNode.get("branches");
        List<BranchDTO> list = objectMapper.convertValue(
                listNode,
                new TypeReference<List<BranchDTO>>() {
                });
        assertEquals(list.size(), 2);

        // Update business
        String fakeEmail = UUID.randomUUID().toString() + "_test@fake.com";
        dto = BusinessDTO.builder()
                .id(1L)
                .userId(1L)
                .email(fakeEmail)
                .name("New name")
                .verified(true)
                .tier("premium")
                .phoneNumber("new phone")
                .build();
        response = mockMvc
                .perform(put((BusinessStatics.Endpoint.PATH + BusinessStatics.Endpoint.UPDATE).replace(
                        "{id}",
                        id.toString()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(id)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(email)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is("New name")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.verified", CoreMatchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.tier", CoreMatchers.is("premium")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber",
                        CoreMatchers.is("new phone")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId", CoreMatchers.is(userId)))
                .andReturn();
        responseJson = response.getResponse().getContentAsString();
        responseNode = objectMapper.readTree(responseJson);

        // Delete business
        mockMvc.perform(delete((BusinessStatics.Endpoint.PATH + BusinessStatics.Endpoint.DELETE).replace("{id}",
                id.toString()))
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

        // Cannot get business by id
        mockMvc.perform(get((BusinessStatics.Endpoint.PATH + BusinessStatics.Endpoint.GET_BY_ID).replace("{id}",
                id.toString()))
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Business with id " + id + " does not exists")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(28)));
    }

    @Test
    public void saveExceptions() throws Exception {
        String email = UUID.randomUUID().toString() + "_test@test.com";
        String password = "123456789";
        BusinessDTO dto = BusinessDTO.builder()
                .email(email)
                .name("Test name")
                .verified(false)
                .tier("basic")
                .phoneNumber("Test phone")
                .build();

        // No token exception
        mockMvc.perform(post(BusinessStatics.Endpoint.PATH + BusinessStatics.Endpoint.SAVE)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Authentication failed")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code",
                        CoreMatchers.is(9)));

        // Invalid token
        mockMvc.perform(post(BusinessStatics.Endpoint.PATH + BusinessStatics.Endpoint.SAVE)
                .header("Authorization", "Bearer a")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Authentication failed")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code",
                        CoreMatchers.is(9)));

        // Create client user
        SignupRequestDTO signupRequest = SignupRequestDTO.builder()
                .email(UUID.randomUUID().toString() + "_test@test.com")
                .password("123456789")
                .role("client")
                .build();
        MvcResult response = mockMvc
                .perform(post(AuthenticationStatics.Endpoint.AUTH_PATH
                        + AuthenticationStatics.Endpoint.SIGNUP)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String responseJson = response.getResponse().getContentAsString();
        JsonNode responseNode = objectMapper.readTree(responseJson);
        String clientToken = responseNode.get("token").asText();

        // Invalid role
        mockMvc.perform(get(BusinessStatics.Endpoint.PATH + BusinessStatics.Endpoint.GET_ALL)
                .header("Authorization", "Bearer " + clientToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));

        // No user exception
        mockMvc.perform(post(BusinessStatics.Endpoint.PATH + BusinessStatics.Endpoint.SAVE)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + businessToken)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(30)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("User with email " + dto.getEmail()
                                + " does not exists")));

        // Create valid user
        signupRequest = SignupRequestDTO.builder()
                .email(email)
                .password(password)
                .role("business")
                .build();
        response = mockMvc
                .perform(post(AuthenticationStatics.Endpoint.AUTH_PATH
                        + AuthenticationStatics.Endpoint.SIGNUP)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        responseJson = response.getResponse().getContentAsString();
        responseNode = objectMapper.readTree(responseJson);
        String token = responseNode.get("token").asText();

        // Invalid tier
        dto.setTier("fake_tier");
        mockMvc.perform(post(BusinessStatics.Endpoint.PATH + BusinessStatics.Endpoint.SAVE)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("The tier given is not valid")));

        // Create business
        dto.setTier("basic");
        mockMvc.perform(post(BusinessStatics.Endpoint.PATH + BusinessStatics.Endpoint.SAVE)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(email)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(dto.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.verified",
                        CoreMatchers.is(dto.getVerified())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.tier", CoreMatchers.is("basic")));

        // Business already exists
        mockMvc.perform(post(BusinessStatics.Endpoint.PATH + BusinessStatics.Endpoint.SAVE)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(12)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Business with email " + dto.getEmail()
                                + " already exists")));
    }

    @Test
    public void getByIdExceptions() throws Exception {
        // No token exception
        mockMvc.perform(get((BusinessStatics.Endpoint.PATH + BusinessStatics.Endpoint.GET_BY_ID).replace("{id}",
                "1"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Authentication failed")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code",
                        CoreMatchers.is(9)));

        // Invalid token
        mockMvc.perform(get((BusinessStatics.Endpoint.PATH + BusinessStatics.Endpoint.GET_BY_ID).replace("{id}",
                "1"))
                .header("Authorization", "Bearer a")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Authentication failed")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code",
                        CoreMatchers.is(9)));

        // Business don't exists
        mockMvc.perform(get((BusinessStatics.Endpoint.PATH + BusinessStatics.Endpoint.GET_BY_ID).replace("{id}",
                "0"))
                .header("Authorization", "Bearer " + businessToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(28)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Business with id 0 does not exists")));
    }

    @Test
    public void updateExceptions() throws Exception {
        // No token exception
        mockMvc.perform(put(
                (BusinessStatics.Endpoint.PATH + BusinessStatics.Endpoint.UPDATE).replace("{id}", "1"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Authentication failed")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code",
                        CoreMatchers.is(9)));

        // Invalid token
        mockMvc.perform(put(
                (BusinessStatics.Endpoint.PATH + BusinessStatics.Endpoint.UPDATE).replace("{id}", "1"))
                .header("Authorization", "Bearer a")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Authentication failed")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code",
                        CoreMatchers.is(9)));

        String email = UUID.randomUUID().toString() + "_test@test.com";
        String password = "123456789";
        BusinessDTO dto = BusinessDTO.builder()
                .email(email)
                .name("Test name")
                .verified(true)
                .tier("basic")
                .build();

        // Token don't match with the business to edit
        mockMvc.perform(put(
                (BusinessStatics.Endpoint.PATH + BusinessStatics.Endpoint.UPDATE).replace("{id}", "0"))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + businessToken)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));

        // Create user
        SignupRequestDTO signupRequest = SignupRequestDTO.builder()
                .email(email)
                .password(password)
                .role("business")
                .build();
        MvcResult response = mockMvc
                .perform(post(AuthenticationStatics.Endpoint.AUTH_PATH
                        + AuthenticationStatics.Endpoint.SIGNUP)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String responseJson = response.getResponse().getContentAsString();
        JsonNode responseNode = objectMapper.readTree(responseJson);
        Integer userId = Integer.parseInt(responseNode.get("id").asText());
        String token = responseNode.get("token").asText();

        // Create business
        response = mockMvc.perform(post(BusinessStatics.Endpoint.PATH + BusinessStatics.Endpoint.SAVE)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(email)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is("Test name")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.verified", CoreMatchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.tier", CoreMatchers.is("basic")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId", CoreMatchers.is(userId)))
                .andReturn();
        responseJson = response.getResponse().getContentAsString();
        responseNode = objectMapper.readTree(responseJson);
        Integer id = Integer.parseInt(responseNode.get("id").asText());

        // Invalid role
        dto.setTier("fake_tier");
        mockMvc.perform(put((BusinessStatics.Endpoint.PATH + BusinessStatics.Endpoint.UPDATE).replace("{id}",
                id.toString()))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("The tier given is not valid")));
    }

    @Test
    public void deleteExceptions() throws Exception {
        // No token exception
        mockMvc.perform(put(
                (BusinessStatics.Endpoint.PATH + BusinessStatics.Endpoint.DELETE).replace("{id}", "1"))
                .header("Authorization", "Bearer a")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Authentication failed")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code",
                        CoreMatchers.is(9)));

        // Invalid token
        mockMvc.perform(put(
                (BusinessStatics.Endpoint.PATH + BusinessStatics.Endpoint.DELETE).replace("{id}", "1"))
                .header("Authorization", "Bearer a")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Authentication failed")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code",
                        CoreMatchers.is(9)));

        // Token dont match with business to edit
        mockMvc.perform(put(
                (BusinessStatics.Endpoint.PATH + BusinessStatics.Endpoint.DELETE).replace("{id}", "0"))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + businessToken))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
    }

    @Test
    public void getByUserIdExceptions() throws Exception {
        // No token exception
        mockMvc.perform(
                get((BusinessStatics.Endpoint.PATH + BusinessStatics.Endpoint.GET_BY_USER_ID)
                        .replace("{id}", "1"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Authentication failed")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code",
                        CoreMatchers.is(9)));

        // Invalid token
        mockMvc.perform(
                get((BusinessStatics.Endpoint.PATH + BusinessStatics.Endpoint.GET_BY_USER_ID)
                        .replace("{id}", "1"))
                        .header("Authorization", "Bearer a")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Authentication failed")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code",
                        CoreMatchers.is(9)));

        // Business don't exists
        mockMvc.perform(
                get((BusinessStatics.Endpoint.PATH + BusinessStatics.Endpoint.GET_BY_USER_ID)
                        .replace("{id}", "0"))
                        .header("Authorization", "Bearer " + businessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(12)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("User with id 0 does not exists")));
    }
}
