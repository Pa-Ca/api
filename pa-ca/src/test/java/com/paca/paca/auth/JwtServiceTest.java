package com.paca.paca.auth;

import org.mockito.Mock;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import com.paca.paca.user.model.Role;
import com.paca.paca.user.model.User;
import com.paca.paca.statics.UserRole;
import com.paca.paca.auth.service.JwtService;
import com.paca.paca.auth.model.JwtBlackList;
import com.paca.paca.auth.repository.JwtBlackListRepository;
import org.springframework.test.context.TestPropertySource;

import java.util.Date;
import java.util.Optional;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
@TestPropertySource(properties = {
        "auth.expiration.token=1000",
        "auth.expiration.refresh=2000",
        "auth.expiration.reset.password=3000",
        "auth.expiration.verify.email=4000",
        "auth.secret.key=test"
})
public class JwtServiceTest {

    @Mock
    private JwtBlackListRepository jwtBlackListRepository;

    @InjectMocks
    private JwtService jwtService;

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
                .loggedIn(false)
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
                .loggedIn(false)
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
                .loggedIn(false)
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
                .loggedIn(false)
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
                .loggedIn(false)
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