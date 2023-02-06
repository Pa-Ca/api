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
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
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

        // provided
        Role role = Role.builder().id(0L).name(UserRole.admin).build();
        roleRepository.save(role);

        // given
        User user = User.builder()
                .id(1L)
                .email("example@example.com")
                .password("123456789aA#")
                .role(role)
                .build();
        underTest.save(user);
        System.out.println("####################################################################Here");

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