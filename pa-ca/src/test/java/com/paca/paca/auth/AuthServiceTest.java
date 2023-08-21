package com.paca.paca.auth;

import junit.framework.TestCase;

import org.junit.Assert;
import org.mockito.Mock;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import com.paca.paca.ServiceTest;
import com.paca.paca.auth.dto.*;
import com.paca.paca.user.model.Role;
import com.paca.paca.user.model.User;
import com.paca.paca.statics.UserRole;
import com.paca.paca.exception.exceptions.*;
import com.paca.paca.auth.service.JwtService;
import com.paca.paca.mail.service.MailService;
import com.paca.paca.auth.service.AuthenticationService;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

public class AuthServiceTest extends ServiceTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private MailService mailService;

    @InjectMocks
    private AuthenticationService authenticationService;

    @Test
    void shouldGetUnprocessableDueToInvalidEmailInSignup() {
        String email = "testtest.com";
        String password = "123456789aA#";
        SignupRequestDTO request = SignupRequestDTO.builder()
                .email(email)
                .password(password)
                .role(UserRole.admin.name())
                .build();

        try {
            authenticationService.signup(request);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof UnprocessableException);
            Assert.assertEquals(e.getMessage(), "Invalid email format");
            Assert.assertEquals(((UnprocessableException) e).getCode(), (Integer) 0);
        }
    }

    @Test
    void shouldGetConflictDueToUserAlreadyExistsInSignup() {
        String email = "test@test.com";
        String password = "123456789aA#";
        SignupRequestDTO request = SignupRequestDTO.builder()
                .email(email)
                .password(password)
                .role(UserRole.admin.name())
                .build();

        when(userRepository.existsByEmail(any(String.class)))
                .thenReturn(true);

        try {
            authenticationService.signup(request);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof ConflictException);
            Assert.assertEquals(e.getMessage(), "User already exists");
            Assert.assertEquals(((ConflictException) e).getCode(), (Integer) 1);
        }
    }

    @Test
    void shouldGetUnprocessableDueToPasswordTooShortInSignup() {
        String email = "test@test.com";
        String password = "42aA#";
        SignupRequestDTO request = SignupRequestDTO.builder()
                .email(email)
                .password(password)
                .role(UserRole.admin.name())
                .build();

        when(userRepository.existsByEmail(any(String.class)))
                .thenReturn(false);

        try {
            authenticationService.signup(request);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof UnprocessableException);
            Assert.assertEquals(e.getMessage(), "Invalid password");
            Assert.assertEquals(((UnprocessableException) e).getCode(), (Integer) 2);
        }
    }

    @Test
    void shouldGetUnprocessableDueToPasswordTooLongInSignup() {
        String email = "test@test.com";
        String password = "42aA#";
        for (int i = 0; i < 64; i++) {
            password += ".";
        }
        SignupRequestDTO request = SignupRequestDTO.builder()
                .email(email)
                .password(password)
                .role(UserRole.admin.name())
                .build();

        when(userRepository.existsByEmail(any(String.class)))
                .thenReturn(false);

        try {
            authenticationService.signup(request);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof UnprocessableException);
            Assert.assertEquals(e.getMessage(), "Invalid password");
            Assert.assertEquals(((UnprocessableException) e).getCode(), (Integer) 3);
        }
    }

    @Test
    void shouldGetBadRequestDueToInvalidRoleInSignup() {
        String email = "test@test.com";
        String password = "123456789Aa#";
        SignupRequestDTO request = SignupRequestDTO.builder()
                .email(email)
                .password(password)
                .role("role")
                .build();

        when(userRepository.existsByEmail(any(String.class)))
                .thenReturn(false);

        try {
            authenticationService.signup(request);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof BadRequestException);
            Assert.assertEquals(e.getMessage(), "The role given is not valid");
        }
    }

    @Test
    void shouldSignup() {
        Long id = 1L;
        String email = "test@test.com";
        String password = "123456789aA#";
        String passHash = "$2a$10$3JJoqbyYXtUDCWt9.H7wKOXeBsEAv3R5uf30qA/8QtCu9GFjqjWSa";
        String token = "eyJhbGciOiJIUzI1NiJ9..._9L5L9hJXCX4WPgpks";
        Role role = Role.builder()
                .id((long) UserRole.client.ordinal())
                .name(UserRole.client).build();
        User user = User.builder()
                .id(id)
                .email(email)
                .password(password)
                .verified(false)
                .role(role)
                .build();
        LoginResponseDTO expected = LoginResponseDTO.builder()
                .id(id)
                .role(role.getName().name())
                .token(token)
                .refresh(token)
                .build();

        when(userRepository.existsByEmail(any(String.class)))
                .thenReturn(false);
        when(roleRepository.findByName(any(UserRole.class)))
                .thenReturn(Optional.ofNullable(role));
        when(passwordEncoder.encode(any(String.class)))
                .thenReturn(passHash);
        when(jwtService.generateToken(any(User.class), any(JwtService.TokenType.class)))
                .thenReturn(token);
        when(userRepository.save(any(User.class)))
                .thenReturn(user);

        SignupRequestDTO request = SignupRequestDTO.builder()
                .email(email)
                .password(password)
                .role(UserRole.client.name())
                .build();
        LoginResponseDTO response = authenticationService.signup(request);

        assertThat(response).isEqualTo(expected);
    }

    @Test
    void shouldGetForbiddenDueToUserDoesNotExistsInLogin() {
        String email = "test@test.com";
        String password = "123456789aA#";

        when(userRepository.existsByEmail(any(String.class)))
                .thenReturn(false);

        LoginRequestDTO request = LoginRequestDTO.builder()
                .email(email)
                .password(password)
                .build();

        try {
            authenticationService.login(request);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof ForbiddenException);
            Assert.assertEquals(e.getMessage(), "Authentication failed");
            Assert.assertEquals(((ForbiddenException) e).getCode(), (Integer) 9);
        }
    }

    @Test
    void shouldGetForbiddenDueToIncorrectPasswordInLogin() {
        Long id = 1L;
        String email = "test@test.com";
        String password = "123456789aA#";
        Role role = Role.builder()
                .id((long) UserRole.admin.ordinal())
                .name(UserRole.admin).build();
        User user = User.builder()
                .id(id)
                .email(email)
                .password(password)
                .verified(false)
                .role(role)
                .build();

        when(userRepository.existsByEmail(any(String.class)))
                .thenReturn(true);
        when(userRepository.findByEmail(any(String.class)))
                .thenReturn(Optional.ofNullable(user));
        when(authenticationManager.authenticate(any(Authentication.class)))
                .thenThrow(new AuthException("Exception"));

        LoginRequestDTO request = LoginRequestDTO.builder()
                .email(email)
                .password(password)
                .build();

        try {
            authenticationService.login(request);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof ForbiddenException);
            Assert.assertEquals(e.getMessage(), "Authentication failed");
            Assert.assertEquals(((ForbiddenException) e).getCode(), (Integer) 9);
        }
    }

    @Test
    void shouldLogin() {
        Long id = 1L;
        String email = "test@test.com";
        String password = "123456789aA#";
        String token = "eyJhbGciOiJIUzI1NiJ9..._9L5L9hJXCX4WPgpks";
        Role role = Role.builder()
                .id((long) UserRole.admin.ordinal())
                .name(UserRole.admin).build();
        User user = User.builder()
                .id(id)
                .email(email)
                .password(password)
                .verified(false)
                .role(role)
                .build();
        Authentication auth = new UsernamePasswordAuthenticationToken(email, password);
        LoginResponseDTO expected = LoginResponseDTO.builder()
                .id(id)
                .role(role.getName().name())
                .token(token)
                .refresh(token)
                .build();

        when(userRepository.existsByEmail(any(String.class)))
                .thenReturn(true);
        when(userRepository.findByEmail(any(String.class)))
                .thenReturn(Optional.ofNullable(user));
        when(authenticationManager.authenticate(any(Authentication.class)))
                .thenReturn(auth);
        when(jwtService.generateToken(any(User.class), any(JwtService.TokenType.class)))
                .thenReturn(token);

        LoginRequestDTO request = LoginRequestDTO.builder()
                .email(email)
                .password(password)
                .build();
        LoginResponseDTO response = authenticationService.login(request);

        assertThat(response).isEqualTo(expected);
    }

    @Test
    void shouldGetForbiddenDueToEmailNotInTokenInRefresh() {
        String refresh = "eyJhbGciOiJIUzI1NiJ9..._9L5L9hJXCX4WPgpks";

        when(jwtService.extractEmail(any(String.class)))
                .thenReturn(null);

        RefreshRequestDTO request = RefreshRequestDTO.builder()
                .refresh(refresh)
                .build();

        try {
            authenticationService.refresh(request);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof ForbiddenException);
            Assert.assertEquals(e.getMessage(), "Authentication failed");
            Assert.assertEquals(((ForbiddenException) e).getCode(), (Integer) 9);
        }
    }

    @Test
    void shouldGetForbiddenDueToUserNotFoundInRefresh() {
        String email = "test@test.com";
        String refresh = "eyJhbGciOiJIUzI1NiJ9..._9L5L9hJXCX4WPgpks";

        when(jwtService.extractEmail(any(String.class)))
                .thenReturn(email);
        when(userRepository.findByEmail(any(String.class)))
                .thenReturn(Optional.empty());

        RefreshRequestDTO request = RefreshRequestDTO.builder()
                .refresh(refresh)
                .build();

        try {
            authenticationService.refresh(request);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof ForbiddenException);
            Assert.assertEquals(e.getMessage(), "Authentication failed");
            Assert.assertEquals(((ForbiddenException) e).getCode(), (Integer) 9);
        }
    }

    @Test
    void shouldGetForbiddenDueToRefreshNoValidInRefresh() {
        Long id = 1L;
        String email = "test@test.com";
        String password = "123456789aA#";
        String refresh = "eyJhbGciOiJIUzI1NiJ9..._9L5L9hJXCX4WPgpks";
        Role role = Role.builder()
                .id((long) UserRole.admin.ordinal())
                .name(UserRole.admin).build();
        User user = User.builder()
                .id(id)
                .email(email)
                .password(password)
                .verified(false)
                .role(role)
                .build();

        when(jwtService.extractEmail(any(String.class)))
                .thenReturn(email);
        when(userRepository.findByEmail(any(String.class)))
                .thenReturn(Optional.ofNullable(user));
        when(jwtService.isTokenValid(any(String.class), any(User.class)))
                .thenReturn(false);

        RefreshRequestDTO request = RefreshRequestDTO.builder()
                .refresh(refresh)
                .build();

        try {
            authenticationService.refresh(request);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof ForbiddenException);
            Assert.assertEquals(e.getMessage(), "Authentication failed");
            Assert.assertEquals(((ForbiddenException) e).getCode(), (Integer) 9);
        }
    }

    @Test
    void shouldGetForbiddenDueToTokenNotIsRefreshInRefresh() {
        Long id = 1L;
        String email = "test@test.com";
        String password = "123456789aA#";
        String refresh = "eyJhbGciOiJIUzI1NiJ9..._9L5L9hJXCX4WPgpks";
        Role role = Role.builder()
                .id((long) UserRole.admin.ordinal())
                .name(UserRole.admin).build();
        User user = User.builder()
                .id(id)
                .email(email)
                .password(password)
                .verified(false)
                .role(role)
                .build();

        when(jwtService.extractEmail(any(String.class)))
                .thenReturn(email);
        when(userRepository.findByEmail(any(String.class)))
                .thenReturn(Optional.ofNullable(user));
        when(jwtService.isTokenValid(any(String.class), any(User.class)))
                .thenReturn(true);
        when(jwtService.isTokenRefresh(any(String.class)))
                .thenReturn(false);

        RefreshRequestDTO request = RefreshRequestDTO.builder()
                .refresh(refresh)
                .build();

        try {
            authenticationService.refresh(request);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof ForbiddenException);
            Assert.assertEquals(e.getMessage(), "Authentication failed");
            Assert.assertEquals(((ForbiddenException) e).getCode(), (Integer) 9);
        }
    }

    @Test
    void shouldRefresh() {
        Long id = 1L;
        String email = "test@test.com";
        String password = "123456789aA#";
        String refresh = "eyJhbGciOiJIUzI1NiJ9..._9L5L9hJXCX4WPgpks";
        String token = "eyJhbGciOiJIUzI1NiJ9...NMJGNh4t8nb8BAD8HY";
        Role role = Role.builder()
                .id((long) UserRole.admin.ordinal())
                .name(UserRole.admin).build();
        User user = User.builder()
                .id(id)
                .email(email)
                .password(password)
                .verified(false)
                .role(role)
                .build();
        RefreshResponseDTO expected = RefreshResponseDTO.builder()
                .token(token)
                .build();

        when(jwtService.extractEmail(any(String.class)))
                .thenReturn(email);
        when(userRepository.findByEmail(any(String.class)))
                .thenReturn(Optional.ofNullable(user));
        when(jwtService.isTokenValid(any(String.class), any(User.class)))
                .thenReturn(true);
        when(jwtService.isTokenRefresh(any(String.class)))
                .thenReturn(true);
        when(jwtService.generateToken(any(User.class), any(JwtService.TokenType.class)))
                .thenReturn(token);

        RefreshRequestDTO request = RefreshRequestDTO.builder()
                .refresh(refresh)
                .build();
        RefreshResponseDTO response = authenticationService.refresh(request);

        assertThat(response).isEqualTo(expected);
    }

    @Test
    void shouldGetBadRequestDueToMissingEmailInResetPasswordRequest() {
        ResetPasswordRequestDTO request = ResetPasswordRequestDTO.builder().build();

        try {
            authenticationService.resetPasswordRequest(request);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof BadRequestException);
            Assert.assertEquals(e.getMessage(), "Email not found");
        }
    }

    @Test
    void shouldGetNoContentDueToUserDoesNotExistsInResetPasswordRequest() {
        String email = "test@test.com";

        ResetPasswordRequestDTO request = ResetPasswordRequestDTO.builder()
                .email(email)
                .build();

        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.empty());

        try {
            authenticationService.resetPasswordRequest(request);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "User with email " + email + " does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 30);
        }
    }

    @Test
    void shouldResetPasswordRequest() {
        Long id = 1L;
        String email = "test@test.com";
        String password = "123456789aA#";
        String token = "eyJhbGciOiJIUzI1NiJ9..._9L5L9hJXCX4WPgpks";
        Role role = Role.builder()
                .id((long) UserRole.admin.ordinal())
                .name(UserRole.admin).build();
        User user = User.builder()
                .id(id)
                .email(email)
                .password(password)
                .verified(false)
                .role(role)
                .build();
        ResetPasswordResponseDTO expected = ResetPasswordResponseDTO.builder()
                .token(token)
                .build();

        ResetPasswordRequestDTO request = ResetPasswordRequestDTO.builder()
                .email(email)
                .build();

        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.ofNullable(user));
        when(jwtService.generateToken(any(User.class), any(JwtService.TokenType.class)))
                .thenReturn(token);

        ResetPasswordResponseDTO response = authenticationService.resetPasswordRequest(request);

        assertThat(response).isEqualTo(expected);
    }

    @Test
    void shouldGetForbiddenDueToMissingTokenAndMissingEmail() {
        String password = "12345678";

        ResetPasswordDTO request = ResetPasswordDTO.builder()
                .newPassword(password)
                .build();

        try {
            authenticationService.resetPassword(request, null);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof ForbiddenException);
            Assert.assertEquals(e.getMessage(), "Authentication failed");
            Assert.assertEquals(((ForbiddenException) e).getCode(), (Integer) 9);
        }
    }

    @Test
    void shouldGetForbiddenDueToInvalidTokenInResetPassword() {
        String password = "12345678";
        String token = "eyJhbGciOiJIUzI1NiJ9..._9L5L9hJXCX4WPgpks";

        ResetPasswordDTO request = ResetPasswordDTO.builder()
                .newPassword(password)
                .build();

        when(jwtService.isTokenValid(any(String.class)))
                .thenReturn(false);

        try {
            authenticationService.resetPassword(request, token);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof ForbiddenException);
            Assert.assertEquals(e.getMessage(), "Authentication failed");
            Assert.assertEquals(((ForbiddenException) e).getCode(), (Integer) 9);
        }
    }

    @Test
    void shouldGetForbiddenDueToIncorrectTokenInResetPassword() {
        String password = "12345678";
        String token = "eyJhbGciOiJIUzI1NiJ9..._9L5L9hJXCX4WPgpks";

        ResetPasswordDTO request = ResetPasswordDTO.builder()
                .newPassword(password)
                .build();

        when(jwtService.isTokenValid(any(String.class)))
                .thenReturn(true);
        when(jwtService.isTokenResetPassword(any(String.class)))
                .thenReturn(false);

        try {
            authenticationService.resetPassword(request, token);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof ForbiddenException);
            Assert.assertEquals(e.getMessage(), "Authentication failed");
            Assert.assertEquals(((ForbiddenException) e).getCode(), (Integer) 9);
        }
    }

    @Test
    void shouldGetUnprocessableDueToPasswordTooShortInResetPassword() {
        String password = "1234";
        String token = "eyJhbGciOiJIUzI1NiJ9..._9L5L9hJXCX4WPgpks";

        ResetPasswordDTO request = ResetPasswordDTO.builder()
                .newPassword(password)
                .build();

        when(jwtService.isTokenValid(any(String.class)))
                .thenReturn(true);
        when(jwtService.isTokenResetPassword(any(String.class)))
                .thenReturn(true);

        try {
            authenticationService.resetPassword(request, token);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof UnprocessableException);
            Assert.assertEquals(e.getMessage(), "Invalid password");
            Assert.assertEquals(((UnprocessableException) e).getCode(), (Integer) 2);
        }
    }

    @Test
    void shouldGetUnprocessableDueToPasswordTooLongInResetPassword() {
        String password = "1234";
        String token = "eyJhbGciOiJIUzI1NiJ9..._9L5L9hJXCX4WPgpks";
        for (int i = 0; i < 64; i++) {
            password += ".";
        }

        ResetPasswordDTO request = ResetPasswordDTO.builder()
                .newPassword(password)
                .build();

        when(jwtService.isTokenValid(any(String.class)))
                .thenReturn(true);
        when(jwtService.isTokenResetPassword(any(String.class)))
                .thenReturn(true);

        try {
            authenticationService.resetPassword(request, token);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof UnprocessableException);
            Assert.assertEquals(e.getMessage(), "Invalid password");
            Assert.assertEquals(((UnprocessableException) e).getCode(), (Integer) 3);
        }
    }

    @Test
    void shouldGetNoContentDueToUserDoesNotExistsInResetPassword() {
        String password = "12345678";
        String email = "test@test.com";
        String token = "eyJhbGciOiJIUzI1NiJ9..._9L5L9hJXCX4WPgpks";

        ResetPasswordDTO request = ResetPasswordDTO.builder()
                .newPassword(password)
                .build();

        when(jwtService.isTokenValid(any(String.class)))
                .thenReturn(true);
        when(jwtService.isTokenResetPassword(any(String.class)))
                .thenReturn(true);
        when(jwtService.extractEmail(any(String.class)))
                .thenReturn(email);
        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.empty());

        try {
            authenticationService.resetPassword(request, token);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "User with email " + email + " does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 30);
        }
    }

    @Test
    void shouldGetForbiddenDueToInvalidOldPasswordInResetPassword() {
        String password = "12345678";
        String email = "test@test.com";

        ResetPasswordDTO request = ResetPasswordDTO.builder()
                .email(email)
                .oldPassword(password)
                .newPassword(password)
                .build();

        when(authenticationManager.authenticate(any(Authentication.class)))
                .thenThrow(new AuthException("Exception"));

        try {
            authenticationService.resetPassword(request, null);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof ForbiddenException);
            Assert.assertEquals(e.getMessage(), "Authentication failed");
            Assert.assertEquals(((ForbiddenException) e).getCode(), (Integer) 9);
        }
    }

    @Test
    void shouldResetPassword() {
        Long id = 1L;
        String email = "test@test.com";
        String password = "123456789aA#";
        String token = "eyJhbGciOiJIUzI1NiJ9..._9L5L9hJXCX4WPgpks";
        Role role = Role.builder()
                .id((long) UserRole.admin.ordinal())
                .name(UserRole.admin).build();
        User user = User.builder()
                .id(id)
                .email(email)
                .password(password)
                .verified(false)
                .role(role)
                .build();

        // Reset password with token
        ResetPasswordDTO request = ResetPasswordDTO.builder()
                .newPassword(password)
                .build();

        when(jwtService.isTokenValid(any(String.class)))
                .thenReturn(true);
        when(jwtService.isTokenResetPassword(any(String.class)))
                .thenReturn(true);
        when(jwtService.extractEmail(any(String.class)))
                .thenReturn(email);
        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.ofNullable(user));

        authenticationService.resetPassword(request, token);

        // Reset password with old password
        request = ResetPasswordDTO.builder()
                .email(email)
                .oldPassword(password)
                .newPassword(password)
                .build();

        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.ofNullable(user));
        when(authenticationManager.authenticate(any(Authentication.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(email, password));

        authenticationService.resetPassword(request, null);
    }

    @Test
    void shouldGetBadRequestDueToMissingEmailInVerifyEmailRequest() {
        VerifyEmailRequestDTO request = VerifyEmailRequestDTO.builder().build();

        try {
            authenticationService.verifyEmailRequest(request);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof BadRequestException);
            Assert.assertEquals(e.getMessage(), "Email not found");
        }
    }

    @Test
    void shouldGetNoContentDueToUserDoesNotExistsInVerifyEmailRequest() {
        String email = "test@test.com";

        VerifyEmailRequestDTO request = VerifyEmailRequestDTO.builder().email(email).build();

        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.empty());

        try {
            authenticationService.verifyEmailRequest(request);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "User with email " + email + " does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 30);
        }
    }

    @Test
    void shouldVerifyEmailRequest() {
        Long id = 1L;
        String email = "test@test.com";
        String password = "123456789aA#";
        String token = "eyJhbGciOiJIUzI1NiJ9..._9L5L9hJXCX4WPgpks";
        Role role = Role.builder()
                .id((long) UserRole.admin.ordinal())
                .name(UserRole.admin).build();
        User user = User.builder()
                .id(id)
                .email(email)
                .password(password)
                .verified(false)
                .role(role)
                .build();
        VerifyEmailResponseDTO expected = VerifyEmailResponseDTO.builder()
                .token(token)
                .build();

        VerifyEmailRequestDTO request = VerifyEmailRequestDTO.builder()
                .email(email)
                .build();

        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.ofNullable(user));
        when(jwtService.generateToken(any(User.class), any(JwtService.TokenType.class)))
                .thenReturn(token);

        VerifyEmailResponseDTO response = authenticationService.verifyEmailRequest(request);

        assertThat(response).isEqualTo(expected);
    }

    @Test
    void shouldGetForbiddenDueToInvalidTokenInVerifyEmail() {
        String token = "eyJhbGciOiJIUzI1NiJ9..._9L5L9hJXCX4WPgpks";

        when(jwtService.isTokenValid(any(String.class))).thenReturn(false);

        try {
            authenticationService.verifyEmail(token);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof ForbiddenException);
            Assert.assertEquals(e.getMessage(), "Authentication failed");
            Assert.assertEquals(((ForbiddenException) e).getCode(), (Integer) 9);
        }
    }

    @Test
    void shouldGetForbiddenDueToIncorrectTokenInVerifyEmail() {
        String token = "eyJhbGciOiJIUzI1NiJ9..._9L5L9hJXCX4WPgpks";

        when(jwtService.isTokenValid(any(String.class))).thenReturn(true);
        when(jwtService.isTokenVerifyEmail(any(String.class))).thenReturn(false);

        try {
            authenticationService.verifyEmail(token);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof ForbiddenException);
            Assert.assertEquals(e.getMessage(), "Authentication failed");
            Assert.assertEquals(((ForbiddenException) e).getCode(), (Integer) 9);
        }
    }

    @Test
    void shouldGetNoContentDueToUserDoesNotExistsInVerifyEmail() {
        String email = "test@test.com";
        String token = "eyJhbGciOiJIUzI1NiJ9..._9L5L9hJXCX4WPgpks";

        when(jwtService.isTokenValid(any(String.class)))
                .thenReturn(true);
        when(jwtService.isTokenVerifyEmail(any(String.class)))
                .thenReturn(true);
        when(jwtService.extractEmail(any(String.class)))
                .thenReturn(email);
        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.empty());

        try {
            authenticationService.verifyEmail(token);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "User with email " + email + " does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 30);
        }
    }

    @Test
    void shouldVerifyEmail() {
        Long id = 1L;
        String email = "test@test.com";
        String password = "123456789aA#";
        String token = "eyJhbGciOiJIUzI1NiJ9..._9L5L9hJXCX4WPgpks";
        Role role = Role.builder()
                .id((long) UserRole.admin.ordinal())
                .name(UserRole.admin).build();
        User user = User.builder()
                .id(id)
                .email(email)
                .password(password)
                .verified(false)
                .role(role)
                .build();

        when(jwtService.isTokenValid(any(String.class)))
                .thenReturn(true);
        when(jwtService.isTokenVerifyEmail(any(String.class)))
                .thenReturn(true);
        when(jwtService.extractEmail(any(String.class)))
                .thenReturn(email);
        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.ofNullable(user));

        authenticationService.verifyEmail(token);
    }
}