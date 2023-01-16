package com.paca.paca.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository underTest;

    @Test
    void itShouldSelectById() {
        // given
        User user = new User(
            1,
            "user@example.com",
            "pass-example"
        );
        underTest.save(user);

        // when
        boolean expected = underTest.existsById(1L);

        // then
        assertThat(expected).isTrue();
    }

}