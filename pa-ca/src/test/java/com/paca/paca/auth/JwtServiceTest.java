package com.paca.paca.auth;

import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;

import com.paca.paca.ServiceTest;
import com.paca.paca.user.model.Role;
import com.paca.paca.user.model.User;
import com.paca.paca.statics.UserRole;
import com.paca.paca.auth.service.JwtService;
import com.paca.paca.auth.model.JwtBlackList;

import java.util.Date;
import java.util.Optional;

import io.github.cdimascio.dotenv.Dotenv;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class JwtServiceTest extends ServiceTest {

    @InjectMocks
    private JwtService jwtService;

    @BeforeEach
    public void setJetServiceProperties() {
        Dotenv dotenv = Dotenv.load();
        jwtService.setSECRET_KEY(dotenv.get("AUTH_SECRET_KEY"));
        jwtService.setEXPIRATION_TOKEN(Integer.parseInt(dotenv.get("AUTH_TOKEN_EXPIRATION")));
        jwtService.setEXPIRATION_REFRESH(Integer.parseInt(dotenv.get("AUTH_REFRESH_EXPIRATION")));
        jwtService.setEXPIRATION_VERIFY_EMAIL(Integer.parseInt(dotenv.get("AUTH_VERIFY_EMAIL_EXPIRATION")));
        jwtService.setEXPIRATION_RESET_PASSWORD(Integer.parseInt(dotenv.get("AUTH_RESET_PASSWORD_EXPIRATION")));
    }

    @Test
    public void testPropertiesAreInitialized() {
        // Verify that the properties annotated with @Value in JwtService are not null
        assertNotNull(jwtService.getEXPIRATION_TOKEN());
        assertNotNull(jwtService.getEXPIRATION_REFRESH());
        assertNotNull(jwtService.getEXPIRATION_RESET_PASSWORD());
        assertNotNull(jwtService.getEXPIRATION_VERIFY_EMAIL());
        assertNotNull(jwtService.getSECRET_KEY());
    }

    @Test
    void shouldGenerateToken() {
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

        when(jwtBlackListRepository.findByToken(any(String.class)))
                .thenReturn(Optional.empty());

        String token = jwtService.generateToken(user, JwtService.TokenType.TOKEN);

        assertThat(jwtService.extractEmail(token)).isEqualTo(email);
        assertThat(jwtService.isTokenValid(token, user)).isTrue();
        assertThat(jwtService.isTokenRefresh(token)).isFalse();
    }

    @Test
    void shouldGenerateRefresh() {
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

        when(jwtBlackListRepository.findByToken(any(String.class)))
                .thenReturn(Optional.empty());

        String refresh = jwtService.generateToken(user, JwtService.TokenType.REFRESH);

        assertThat(jwtService.extractEmail(refresh)).isEqualTo(email);
        assertThat(jwtService.isTokenValid(refresh, user)).isTrue();
        assertThat(jwtService.isTokenRefresh(refresh)).isTrue();
    }

    @Test
    void shouldGenerateResetPassword() {
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

        when(jwtBlackListRepository.findByToken(any(String.class)))
                .thenReturn(Optional.empty());

        String refresh = jwtService.generateToken(user, JwtService.TokenType.RESET_PASSWORD);

        assertThat(jwtService.extractEmail(refresh)).isEqualTo(email);
        assertThat(jwtService.isTokenValid(refresh, user)).isTrue();
        assertThat(jwtService.isTokenResetPassword(refresh)).isTrue();
    }

    @Test
    void shouldGenerateVerifyEmail() {
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

        when(jwtBlackListRepository.findByToken(any(String.class)))
                .thenReturn(Optional.empty());

        String refresh = jwtService.generateToken(user, JwtService.TokenType.VERIFY_EMAIL);

        assertThat(jwtService.extractEmail(refresh)).isEqualTo(email);
        assertThat(jwtService.isTokenValid(refresh, user)).isTrue();
        assertThat(jwtService.isTokenVerifyEmail(refresh)).isTrue();
    }

    @Test
    void shouldAddToBlackList() {
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

        String token = jwtService.generateToken(user, JwtService.TokenType.TOKEN);

        Date expiration = new Date(System.currentTimeMillis());
        JwtBlackList jwt = JwtBlackList.builder()
                .id(1L)
                .token(token)
                .expiration(expiration)
                .build();
        when(jwtBlackListRepository.save(any(JwtBlackList.class)))
                .thenReturn(jwt);
        when(jwtBlackListRepository.findByToken(any(String.class)))
                .thenReturn(Optional.ofNullable(jwt));

        jwtService.addTokenToBlackList(token);

        assertThat(jwtService.isTokenValid(token, user)).isFalse();
    }
}