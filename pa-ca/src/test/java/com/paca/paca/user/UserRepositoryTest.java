package com.paca.paca.user;

import com.paca.paca.role.Role;
import com.paca.paca.statics.UserRole;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository underTest;

    @AfterEach
    void restoreTest() {
        underTest.deleteAll();
    }

    @Test
    @Disabled
    void existsUserByEmail() {
        // given
        User user = new User(
            1L,
            "user@example.com",
            "pass-example",
                new Role((long) UserRole.admin.ordinal(), UserRole.admin)
        );
        underTest.save(user);

        // when
        boolean expected = underTest.existsByEmail(user.getEmail());

        // then
        assertThat(expected).isTrue();
    }

    @Test
    @Disabled
    void doesNotExistsUserById() {
        // given
        Long id = 1L;

        // when
        boolean expected = underTest.existsById(id);

        // then
        assertThat(expected).isFalse();
    }

}