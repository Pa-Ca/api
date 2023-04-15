package com.paca.paca.auth;

import junit.framework.TestCase;

import org.junit.Assert;
import org.mockito.Mock;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import com.paca.paca.user.model.Role;
import com.paca.paca.user.model.User;
import com.paca.paca.statics.UserRole;
import com.paca.paca.user.utils.UserMapper;
import com.paca.paca.auth.service.JwtService;
import com.paca.paca.auth.dto.LoginRequestDTO;
import com.paca.paca.auth.dto.LoginResponseDTO;
import com.paca.paca.auth.dto.SignupRequestDTO;
import com.paca.paca.auth.dto.ResetPasswordDTO;
import com.paca.paca.auth.dto.RefreshRequestDTO;
import com.paca.paca.auth.dto.RefreshResponseDTO;
import com.paca.paca.user.repository.RoleRepository;
import com.paca.paca.user.repository.UserRepository;
import com.paca.paca.auth.dto.ResetPasswordRequestDTO;
import com.paca.paca.auth.dto.ResetPasswordResponseDTO;
import com.paca.paca.auth.service.AuthenticationService;
import com.paca.paca.exception.exceptions.AuthException;
import com.paca.paca.exception.exceptions.ConflictException;
import com.paca.paca.auth.repository.JwtBlackListRepository;
import com.paca.paca.exception.exceptions.ForbiddenException;
import com.paca.paca.exception.exceptions.NoContentException;
import com.paca.paca.exception.exceptions.BadRequestException;
import com.paca.paca.exception.exceptions.UnprocessableException;

import java.util.Optional;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private JwtBlackListRepository jwtBlackListRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

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
                .id((long) UserRole.admin.ordinal())
                .name(UserRole.admin).build();
        User user = User.builder()
                .id(id)
                .email(email)
                .password(password)
                .verified(false)
                .loggedIn(false)
                .role(role)
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
                .role(UserRole.admin.name())
                .build();
        LoginResponseDTO response = authenticationService.signup(request);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(user.getId());
        assertThat(response.getRole()).isEqualTo(user.getRole().getName().name());
        assertThat(response.getToken()).isEqualTo(token);
        assertThat(response.getRefresh()).isEqualTo(token);
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
                .loggedIn(false)
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
                .loggedIn(false)
                .role(role)
                .build();
        Authentication auth = new UsernamePasswordAuthenticationToken(email, password);

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

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(user.getId());
        assertThat(response.getRole()).isEqualTo(user.getRole().getName().name());
        assertThat(response.getToken()).isEqualTo(token);
        assertThat(response.getRefresh()).isEqualTo(token);
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
                .loggedIn(false)
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
                .loggedIn(false)
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
                .loggedIn(false)
                .role(role)
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

        assertThat(response).isNotNull();
        assertThat(response.getToken()).isEqualTo(token);
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
                .loggedIn(false)
                .role(role)
                .build();

        ResetPasswordRequestDTO request = ResetPasswordRequestDTO.builder()
                .email(email)
                .build();

        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.ofNullable(user));
        when(jwtService.generateToken(any(User.class), any(JwtService.TokenType.class)))
                .thenReturn(token);

        ResetPasswordResponseDTO response = authenticationService.resetPasswordRequest(request);

        assertThat(response).isNotNull();
        assertThat(response.getToken()).isEqualTo(token);
    }

    @Test
    void shouldGetForbiddenDueToInvalidTokenInResetPassword() {
        String password = "12345678";
        String token = "eyJhbGciOiJIUzI1NiJ9..._9L5L9hJXCX4WPgpks";

        ResetPasswordDTO request = ResetPasswordDTO.builder()
                .password(password)
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
                .password(password)
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
                .password(password)
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
                .password(password)
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
                .password(password)
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
                .loggedIn(false)
                .role(role)
                .build();

        ResetPasswordDTO request = ResetPasswordDTO.builder()
                .password(password)
                .build();

        when(jwtService.isTokenValid(any(String.class)))
                .thenReturn(true);
        when(jwtService.isTokenResetPassword(any(String.class)))
                .thenReturn(true);
        when(jwtService.extractEmail(any(String.class)))
                .thenReturn(email);
        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.ofNullable(user));

        authenticationService.resetPassword(request, token);
    }

}