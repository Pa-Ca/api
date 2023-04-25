package com.paca.paca.auth;

import com.paca.paca.auth.dto.*;
import com.paca.paca.utils.TestUtils;
import com.paca.paca.auth.service.JwtService;
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
    void shouldGetBadRequestInSignup() throws Exception {
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
    void shouldGetNoContentInSignup() throws Exception {
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
    void shouldGetUnprocessableInSignup() throws Exception {
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
    void shouldGetConflictInSignup() throws Exception {
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
    void shouldGetForbiddenInLogin() throws Exception {
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
    void shouldGetForbiddenInRefresh() throws Exception {
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
    void shouldGetBadRequestInLogout() throws Exception {
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

    @Test
    void shouldGetBadRequestExceptionInResetPasswordRequest() throws Exception {
        ResetPasswordRequestDTO request = ResetPasswordRequestDTO.builder()
                .email("test@test.com")
                .build();

        when(authService.resetPasswordRequest(any(ResetPasswordRequestDTO.class)))
                .thenThrow(new BadRequestException("message", 0));

        mockMvc.perform(post(
                AuthenticationStatics.Endpoint.AUTH_PATH.concat(AuthenticationStatics.Endpoint.RESET_PASSWORD_REQUEST))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

    @Test
    void shouldGetNoContentExceptionInResetPasswordRequest() throws Exception {
        ResetPasswordRequestDTO request = ResetPasswordRequestDTO.builder()
                .email("test@test.com")
                .build();

        when(authService.resetPasswordRequest(any(ResetPasswordRequestDTO.class)))
                .thenThrow(new NoContentException("message", 0));

        mockMvc.perform(post(
                AuthenticationStatics.Endpoint.AUTH_PATH.concat(AuthenticationStatics.Endpoint.RESET_PASSWORD_REQUEST))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

    @Test
    void shouldResetPasswordRequest() throws Exception {
        ResetPasswordRequestDTO request = ResetPasswordRequestDTO.builder()
                .email("test@test.com")
                .build();
        ResetPasswordResponseDTO response = ResetPasswordResponseDTO.builder()
                .token("eyJhbGciOiJIUzI1NiJ9..._9L5L9hJXCX4WPgpks")
                .build();

        when(authService.resetPasswordRequest(any(ResetPasswordRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post(
                AuthenticationStatics.Endpoint.AUTH_PATH.concat(AuthenticationStatics.Endpoint.RESET_PASSWORD_REQUEST))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.token", CoreMatchers.is(response.getToken())));
    }

    @Test
    void shouldGetForbiddenExceptionInResetPassword() throws Exception {
        String token = "eyJhbGciOiJIUzI1NiJ9..._9L5L9hJXCX4WPgpks";
        ResetPasswordDTO request = ResetPasswordDTO.builder()
                .password("123455678")
                .build();

        doThrow(new ForbiddenException("message", 0))
                .when(authService).resetPassword(any(ResetPasswordDTO.class),
                        any(String.class));

        mockMvc.perform(post(
                AuthenticationStatics.Endpoint.AUTH_PATH
                        .concat(AuthenticationStatics.Endpoint.RESET_PASSWORD + "/" + token))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

    @Test
    void shouldGetBadRequestExceptionInResetPassword() throws Exception {
        String token = "eyJhbGciOiJIUzI1NiJ9..._9L5L9hJXCX4WPgpks";
        ResetPasswordDTO request = ResetPasswordDTO.builder()
                .password("123455678")
                .build();

        doThrow(new BadRequestException("message", 0))
                .when(authService).resetPassword(any(ResetPasswordDTO.class),
                        any(String.class));

        mockMvc.perform(post(
                AuthenticationStatics.Endpoint.AUTH_PATH
                        .concat(AuthenticationStatics.Endpoint.RESET_PASSWORD + "/" + token))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

    @Test
    void shouldGetUnprocessableExceptionInResetPassword() throws Exception {
        String token = "eyJhbGciOiJIUzI1NiJ9..._9L5L9hJXCX4WPgpks";
        ResetPasswordDTO request = ResetPasswordDTO.builder()
                .password("123455678")
                .build();

        doThrow(new UnprocessableException("message", 0))
                .when(authService).resetPassword(any(ResetPasswordDTO.class),
                        any(String.class));

        mockMvc.perform(post(
                AuthenticationStatics.Endpoint.AUTH_PATH
                        .concat(AuthenticationStatics.Endpoint.RESET_PASSWORD + "/" + token))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

    @Test
    void shouldGetNoContentExceptionInResetPassword() throws Exception {
        String token = "eyJhbGciOiJIUzI1NiJ9..._9L5L9hJXCX4WPgpks";
        ResetPasswordDTO request = ResetPasswordDTO.builder()
                .password("123455678")
                .build();

        doThrow(new NoContentException("message", 0))
                .when(authService).resetPassword(any(ResetPasswordDTO.class),
                        any(String.class));

        mockMvc.perform(post(
                AuthenticationStatics.Endpoint.AUTH_PATH
                        .concat(AuthenticationStatics.Endpoint.RESET_PASSWORD + "/" + token))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

    @Test
    void shouldResetPassword() throws Exception {
        String token = "eyJhbGciOiJIUzI1NiJ9..._9L5L9hJXCX4WPgpks";
        ResetPasswordDTO request = ResetPasswordDTO.builder()
                .password("123455678")
                .build();

        doNothing().when(authService)
                .resetPassword(any(ResetPasswordDTO.class),
                        any(String.class));

        mockMvc.perform(post(
                AuthenticationStatics.Endpoint.AUTH_PATH
                        .concat(AuthenticationStatics.Endpoint.RESET_PASSWORD + "/" + token))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

// adsasdadadadaad
    @Test
    void shouldGetBadRequestExceptionInVerifyEmailRequest() throws Exception {
        VerifyEmailRequestDTO request = VerifyEmailRequestDTO.builder()
                .email("test@test.com")
                .build();

        when(authService.verifyEmailRequest(any(VerifyEmailRequestDTO.class)))
                .thenThrow(new BadRequestException("message", 0));

        mockMvc.perform(post(
                        AuthenticationStatics.Endpoint.AUTH_PATH.concat(AuthenticationStatics.Endpoint.VERIFY_EMAIL_REQUEST))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

    @Test
    void shouldGetNoContentExceptionInVerifyEmailRequest() throws Exception {
        VerifyEmailRequestDTO request = VerifyEmailRequestDTO.builder()
                .email("test@test.com")
                .build();

        when(authService.verifyEmailRequest(any(VerifyEmailRequestDTO.class)))
                .thenThrow(new NoContentException("message", 0));

        mockMvc.perform(post(
                        AuthenticationStatics.Endpoint.AUTH_PATH.concat(AuthenticationStatics.Endpoint.VERIFY_EMAIL_REQUEST))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

    @Test
    void shouldVerifyEmailRequest() throws Exception {
        VerifyEmailRequestDTO request = VerifyEmailRequestDTO.builder()
                .email("test@test.com")
                .build();
        VerifyEmailResponseDTO response = VerifyEmailResponseDTO.builder()
                .token("eyJhbGciOiJIUzI1NiJ9..._9L5L9hJXCX4WPgpks")
                .build();

        when(authService.verifyEmailRequest(any(VerifyEmailRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post(
                        AuthenticationStatics.Endpoint.AUTH_PATH.concat(AuthenticationStatics.Endpoint.VERIFY_EMAIL_REQUEST))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.token", CoreMatchers.is(response.getToken())));
    }

    @Test
    void shouldGetForbiddenExceptionInVerifyEmail() throws Exception {
        String token = "eyJhbGciOiJIUzI1NiJ9..._9L5L9hJXCX4WPgpks";

        doThrow(new ForbiddenException("message", 0))
                .when(authService).verifyEmail(any(String.class));

        mockMvc.perform(post(
                        AuthenticationStatics.Endpoint.AUTH_PATH
                                .concat(AuthenticationStatics.Endpoint.VERIFY_EMAIL + "/" + token))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

    @Test
    void shouldGetBadRequestExceptionInVerifyEmail() throws Exception {
        String token = "eyJhbGciOiJIUzI1NiJ9..._9L5L9hJXCX4WPgpks";

        doThrow(new BadRequestException("message", 0))
                .when(authService).verifyEmail(any(String.class));

        mockMvc.perform(post(
                        AuthenticationStatics.Endpoint.AUTH_PATH
                                .concat(AuthenticationStatics.Endpoint.VERIFY_EMAIL + "/" + token))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

    @Test
    void shouldGetUnprocessableExceptionInVerifyEmail() throws Exception {
        String token = "eyJhbGciOiJIUzI1NiJ9..._9L5L9hJXCX4WPgpks";

        doThrow(new UnprocessableException("message", 0))
                .when(authService).verifyEmail(any(String.class));

        mockMvc.perform(post(
                        AuthenticationStatics.Endpoint.AUTH_PATH
                                .concat(AuthenticationStatics.Endpoint.VERIFY_EMAIL+ "/" + token))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

    @Test
    void shouldGetNoContentExceptionInVerifyEmail() throws Exception {
        String token = "eyJhbGciOiJIUzI1NiJ9..._9L5L9hJXCX4WPgpks";

        doThrow(new NoContentException("message", 0))
                .when(authService).verifyEmail(any(String.class));

        mockMvc.perform(post(
                        AuthenticationStatics.Endpoint.AUTH_PATH
                                .concat(AuthenticationStatics.Endpoint.VERIFY_EMAIL + "/" + token))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

    @Test
    void shouldVerifyEmail() throws Exception {
        String token = "eyJhbGciOiJIUzI1NiJ9..._9L5L9hJXCX4WPgpks";

        doNothing().when(authService)
                .verifyEmail(any(String.class));

        mockMvc.perform(post(
                        AuthenticationStatics.Endpoint.AUTH_PATH
                                .concat(AuthenticationStatics.Endpoint.VERIFY_EMAIL + "/" + token))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

}