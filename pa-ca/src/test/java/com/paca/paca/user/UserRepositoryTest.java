package com.paca.paca.user;

import com.paca.paca.PacaTest;
import com.paca.paca.statics.UserRole;
import com.paca.paca.user.model.Role;
import com.paca.paca.user.model.User;
import com.paca.paca.user.repository.RoleRepository;
import com.paca.paca.user.repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@Testcontainers
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest extends PacaTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @AfterEach
    void restoreTest() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    void shouldCheckThatUserExistsByEmail() {
        String email = "test@test.com";
        Role role = Role.builder().id((long) UserRole.admin.ordinal()).name(UserRole.admin).build();
        User user = User.builder()
                .id(1L)
                .email(email)
                .password("123456789aA#")
                .verified(false)
                .role(role)
                .build();

        roleRepository.save(role);
        userRepository.save(user);

        boolean expected = userRepository.existsByEmail(email);

        assertThat(expected).isTrue();
    }

    @Test
    void shouldCheckThatUserDoesNotExistsByEmail() {
        String email = "test@test.com";
        boolean expected = userRepository.existsByEmail(email);

        assertThat(expected).isFalse();
    }

    @Test
    void shouldReturnUserByEmail() {
        String email = "test@test.com";
        Role role = Role.builder().id((long) UserRole.admin.ordinal()).name(UserRole.admin).build();
        User user = User.builder()
                .id(1L)
                .email(email)
                .password("123456789aA#")
                .verified(false)
                .role(role)
                .build();

        roleRepository.save(role);
        userRepository.save(user);

        Optional<User> response = userRepository.findByEmail(email);

        assertThat(response).isNotEmpty();
        assertThat(response.get().getEmail().equals(email)).isTrue();
    }

    @Test
    void shouldNotReturnUserByEmail() {
        String email = "test@test.com";
        Optional<User> user = userRepository.findByEmail(email);

        assertThat(user).isEmpty();
    }
}