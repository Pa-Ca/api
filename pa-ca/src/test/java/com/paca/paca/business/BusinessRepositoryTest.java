package com.paca.paca.business;

import org.junit.jupiter.api.Test;

import com.paca.paca.RepositoryTest;
import com.paca.paca.user.model.User;
import com.paca.paca.business.model.Business;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.Optional;

class BusinessRepositoryTest extends RepositoryTest {

    @Test
    void shouldCheckThatBusinessExistsByUserId() {
        User user = utils.createUser();
        Business business = utils.createBusiness(user);

        Optional<Business> expectedBusiness = businessRepository.findByUserId(user.getId());

        assertThat(expectedBusiness.get()).isEqualTo(business);
    }

    @Test
    void shouldCheckThatBusinessDoesNotExistsByUserId() {
        User user = utils.createUser();
        utils.createBusiness(user);

        Optional<Business> expectedBusiness = businessRepository.findByUserId(user.getId() + 1);

        assertThat(expectedBusiness.isEmpty()).isTrue();
    }

    @Test
    void shouldCheckThatBusinessExistsByUserEmail() {
        User user = utils.createUser();
        Business business = utils.createBusiness(user);

        boolean expected = businessRepository.existsByUserEmail(user.getEmail());
        Optional<Business> expectedBusiness = businessRepository.findByUserEmail(user.getEmail());

        assertThat(expected).isTrue();
        assertThat(expectedBusiness.get()).isEqualTo(business);
    }

    @Test
    void shouldCheckThatBusinessDoesNotExistsByUserEmail() {
        User user = utils.createUser();
        utils.createBusiness(user);

        boolean expected = businessRepository.existsByUserEmail(user.getEmail() + "a");
        Optional<Business> expectedBusiness = businessRepository.findByUserEmail(user.getEmail() + "a");

        assertThat(expected).isFalse();
        assertThat(expectedBusiness.isEmpty()).isTrue();
    }
}