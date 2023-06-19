package com.paca.paca.auth;

import com.paca.paca.PacaTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import com.paca.paca.auth.model.JwtBlackList;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;
import com.paca.paca.auth.repository.JwtBlackListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@Testcontainers
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AuthRepositoryTest extends PacaTest {

    @Autowired
    private JwtBlackListRepository jwtBlackListRepository;

    @AfterEach
    void restoreTest() {
        jwtBlackListRepository.deleteAll();
    }

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
