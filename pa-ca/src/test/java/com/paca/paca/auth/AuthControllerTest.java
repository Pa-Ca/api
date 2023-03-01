package com.paca.paca.auth;

import com.paca.paca.utils.TestUtils;
import com.paca.paca.auth.dto.LogoutDTO;
import com.paca.paca.auth.service.JwtService;
import com.paca.paca.auth.dto.LoginRequestDTO;
import com.paca.paca.auth.dto.LoginResponseDTO;
import com.paca.paca.auth.dto.SignupRequestDTO;
import com.paca.paca.auth.dto.RefreshRequestDTO;
import com.paca.paca.auth.dto.RefreshResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paca.paca.auth.statics.AuthenticationStatics;
import com.paca.paca.auth.service.AuthenticationService;
import com.paca.paca.exception.exceptions.ConflictException;
import com.paca.paca.exception.exceptions.ForbiddenException;
import com.paca.paca.exception.exceptions.NoContentException;
import com.paca.paca.exception.exceptions.BadRequestException;
import com.paca.paca.auth.controller.AuthenticationController;
import com.paca.paca.exception.exceptions.UnprocessableException;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = { AuthenticationController.class })
public class AuthControllerTest extends ControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private AuthenticationService authService;

    private TestUtils utils = TestUtils.builder().build();

    @Test
    public void shouldGetBadRequestInSignup() throws Exception {
        SignupRequestDTO request = utils.createSignupRequestDTO();

        when(authService.signup(any(SignupRequestDTO.class))).thenThrow(new BadRequestException("message", 0));

        mockMvc.perform(post(AuthenticationStatics.Endpoint.AUTH_PATH.concat(AuthenticationStatics.Endpoint.SIGNUP))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

    @Test
    public void shouldGetNoContentInSignup() throws Exception {
        SignupRequestDTO request = utils.createSignupRequestDTO();

        when(authService.signup(any(SignupRequestDTO.class))).thenThrow(new NoContentException("message", 0));

        mockMvc.perform(post(AuthenticationStatics.Endpoint.AUTH_PATH.concat(AuthenticationStatics.Endpoint.SIGNUP))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

    @Test
    public void shouldGetUnprocessableInSignup() throws Exception {
        SignupRequestDTO request = utils.createSignupRequestDTO();

        when(authService.signup(any(SignupRequestDTO.class))).thenThrow(new UnprocessableException("message", 0));

        mockMvc.perform(post(AuthenticationStatics.Endpoint.AUTH_PATH.concat(AuthenticationStatics.Endpoint.SIGNUP))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

    @Test
    public void shouldGetConflictInSignup() throws Exception {
        SignupRequestDTO request = utils.createSignupRequestDTO();

        when(authService.signup(any(SignupRequestDTO.class))).thenThrow(new ConflictException("message", 0));

        mockMvc.perform(post(AuthenticationStatics.Endpoint.AUTH_PATH.concat(AuthenticationStatics.Endpoint.SIGNUP))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

    @Test 
    void shouldSignup() throws Exception {
        SignupRequestDTO request = utils.createSignupRequestDTO();
        LoginResponseDTO response = utils.createLoginResponseDTO();

        when(authService.signup(any(SignupRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post(AuthenticationStatics.Endpoint.AUTH_PATH.concat(AuthenticationStatics.Endpoint.SIGNUP))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.token", CoreMatchers.is(response.getToken())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.refresh", CoreMatchers.is(response.getRefresh())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(response.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.role", CoreMatchers.is(response.getRole())));
    }

    @Test
    public void shouldGetForbiddenInLogin() throws Exception {
        LoginRequestDTO request = utils.createLoginRequestDTO();

        when(authService.login(any(LoginRequestDTO.class))).thenThrow(new ForbiddenException("message", 0));

        mockMvc.perform(post(AuthenticationStatics.Endpoint.AUTH_PATH.concat(AuthenticationStatics.Endpoint.LOGIN))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

    @Test 
    void shouldLogin() throws Exception {
        LoginRequestDTO request = utils.createLoginRequestDTO();
        LoginResponseDTO response = utils.createLoginResponseDTO();

        when(authService.login(any(LoginRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post(AuthenticationStatics.Endpoint.AUTH_PATH.concat(AuthenticationStatics.Endpoint.LOGIN))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.token", CoreMatchers.is(response.getToken())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.refresh", CoreMatchers.is(response.getRefresh())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(response.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.role", CoreMatchers.is(response.getRole())));
    }

    @Test
    public void shouldGetForbiddenInRefresh() throws Exception {
        RefreshRequestDTO request = utils.createRefreshRequestDTO();

        when(authService.refresh(any(RefreshRequestDTO.class))).thenThrow(new ForbiddenException("message", 0));

        mockMvc.perform(post(AuthenticationStatics.Endpoint.AUTH_PATH.concat(AuthenticationStatics.Endpoint.REFRESH))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

    @Test 
    void shouldRefresh() throws Exception {
        RefreshRequestDTO request = utils.createRefreshRequestDTO();
        RefreshResponseDTO response = utils.createRefreshResponseDTO();

        when(authService.refresh(any(RefreshRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post(AuthenticationStatics.Endpoint.AUTH_PATH.concat(AuthenticationStatics.Endpoint.REFRESH))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.token", CoreMatchers.is(response.getToken())));
    }

    @Test
    public void shouldGetBadRequestInLogout() throws Exception {
        LogoutDTO request = utils.createLogoutDTO();

        doThrow(new ForbiddenException("message", 0)).when(authService).logout(any(LogoutDTO.class));

        mockMvc.perform(post(AuthenticationStatics.Endpoint.AUTH_PATH.concat(AuthenticationStatics.Endpoint.LOGOUT))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

    @Test 
    void shouldLogout() throws Exception {
        LogoutDTO request = utils.createLogoutDTO();

        doNothing().when(authService).logout(any(LogoutDTO.class));

        mockMvc.perform(post(AuthenticationStatics.Endpoint.AUTH_PATH.concat(AuthenticationStatics.Endpoint.LOGOUT))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
