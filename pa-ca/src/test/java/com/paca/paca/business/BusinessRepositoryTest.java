package com.paca.paca.business;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class BusinessRepositoryTest {

    @Autowired
    private BusinessRepository underTest;

    @Test
    void itShouldSelectById() {
        // given
        Business business = new Business(
            1L,
            "McDonald Charallave",
            true
        );
        underTest.save(business);

        // when
        boolean expected = underTest.existsById(1L);

        // then
        assertThat(expected).isTrue();
    }

}