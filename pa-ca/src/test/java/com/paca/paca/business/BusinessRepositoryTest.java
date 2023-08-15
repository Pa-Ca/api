package com.paca.paca.business;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import com.paca.paca.PacaTest;
import com.paca.paca.user.model.User;
import com.paca.paca.utils.TestUtils;
import com.paca.paca.business.model.Business;
import com.paca.paca.user.repository.RoleRepository;
import com.paca.paca.user.repository.UserRepository;
import com.paca.paca.business.repository.TierRepository;
import com.paca.paca.business.repository.BusinessRepository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.Optional;

@DataJpaTest
@Testcontainers
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BusinessRepositoryTest extends PacaTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private TierRepository tierRepository;

    @Autowired
    private BusinessRepository businessRepository;

    private TestUtils utils;

    @BeforeAll
    void initUtils() {
        utils = TestUtils.builder()
                .roleRepository(roleRepository)
                .userRepository(userRepository)
                .tierRepository(tierRepository)
                .businessRepository(businessRepository)
                .build();
    }

    @BeforeEach
    void restoreBusinessDB() {
        businessRepository.deleteAll();
    }

    @AfterEach
    void restoreTest() {
        businessRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();
        tierRepository.deleteAll();
    }

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