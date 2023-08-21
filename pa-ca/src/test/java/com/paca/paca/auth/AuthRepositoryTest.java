package com.paca.paca.auth;

import org.junit.jupiter.api.Test;

import com.paca.paca.RepositoryTest;
import com.paca.paca.auth.model.JwtBlackList;

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class AuthRepositoryTest extends RepositoryTest {

    @Test
    void shouldCheckThatJwtExistsByToken() {
        String token = "eyJhbGciOiJIUzI1NiJ9..._9L5L9hJXCX4WPgpks";
        Date expiration = new Date(System.currentTimeMillis());
        JwtBlackList jwt = JwtBlackList.builder()
                .id(1L)
                .token(token)
                .expiration(expiration)
                .build();
        jwtBlackListRepository.save(jwt);

        boolean expected = jwtBlackListRepository.findByToken(token).isPresent();
        assertThat(expected).isTrue();
    }

    @Test
    void shouldCheckThatJwtDoesNotExistsByJwt() {
        String token = "eyJhbGciOiJIUzI1NiJ9..._9L5L9hJXCX4WPgpks";
        boolean expected = jwtBlackListRepository.findByToken(token).isPresent();

        assertThat(expected).isFalse();
    }

    @Test
    void shouldCheckThatJwtExistsByExpiration() {
        String token = "eyJhbGciOiJIUzI1NiJ9..._9L5L9hJXCX4WPgpks";
        Date expiration = new Date(System.currentTimeMillis());
        JwtBlackList jwt = JwtBlackList.builder()
                .id(1L)
                .token(token)
                .expiration(expiration)
                .build();
        jwtBlackListRepository.save(jwt);
        boolean expected = jwtBlackListRepository.findAllByExpirationLessThan(
                new Date(System.currentTimeMillis() + 100000000)).size() > 0;

        assertThat(expected).isTrue();
    }

    @Test
    void shouldCheckThatJwtDoesNotExistsByExpiration() {
        String token = "eyJhbGciOiJIUzI1NiJ9..._9L5L9hJXCX4WPgpks";
        Date expiration = new Date(System.currentTimeMillis());
        JwtBlackList jwt = JwtBlackList.builder()
                .id(1L)
                .token(token)
                .expiration(expiration)
                .build();
        jwtBlackListRepository.save(jwt);
        boolean expected = jwtBlackListRepository.findAllByExpirationLessThan(
                new Date(System.currentTimeMillis() - 100000000)).size() == 0;

        assertThat(expected).isTrue();
    }

    @Test
    void shouldReturnJwtByToken() {
        String token = "eyJhbGciOiJIUzI1NiJ9..._9L5L9hJXCX4WPgpks";
        Date expiration = new Date(System.currentTimeMillis());
        JwtBlackList jwt = JwtBlackList.builder()
                .id(1L)
                .token(token)
                .expiration(expiration)
                .build();
        jwtBlackListRepository.save(jwt);
        Optional<JwtBlackList> response = jwtBlackListRepository.findByToken(token);

        assertThat(response).isNotEmpty();
        assertThat(response.get().getToken().equals(token)).isTrue();
    }
}
