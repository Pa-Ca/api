package com.paca.paca.user;

import com.paca.paca.statics.UserRole;
import com.paca.paca.user.model.Role;
import com.paca.paca.user.model.User;
import com.paca.paca.user.repository.RoleRepository;
import com.paca.paca.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {
    @Autowired private UserRepository underTest;
    @Autowired private RoleRepository roleRepository;

    @AfterEach
    void restoreTest() {
        underTest.deleteAll();
    }

    @Test
    @Disabled
    void existsUserByEmail() {
        // given
        Role role = Role.builder()
                .id((long) UserRole.admin.ordinal())
                .name(UserRole.admin)
                .build();
        User user = User.builder()
                .id(1L)
                .email("exampleeeeeeeeeeee@example.com")
                .password("123456789aA#")
                .verified(false)
                .loggedIn(false)
                .role(role)
                .build();
        roleRepository.save(role);
        underTest.save(user);

        // when
        boolean expected = underTest.existsByEmail(user.getEmail());

        // then
        assertThat(expected).isTrue();
    }

    @Test
    @Disabled
    void doesNotExistsUserByEmail() {
        // when
        boolean expected = underTest.existsByEmail("nouser@example.com");

        // then
        assertThat(expected).isFalse();
    }
}