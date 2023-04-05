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
import com.paca.paca.business.model.Tier;
import com.paca.paca.statics.BusinessTier;
import com.paca.paca.business.model.Business;
import com.paca.paca.user.repository.RoleRepository;
import com.paca.paca.user.repository.UserRepository;
import com.paca.paca.business.repository.TierRepository;
import com.paca.paca.business.repository.BusinessRepository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@Testcontainers
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BusinessRepositoryTest extends PacaTest {

    @Autowired
    private BusinessRepository businessRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private TierRepository tierRepository;
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
    void shouldCreateBusiness() {
        User user = utils.createUser();
        Tier tier = Tier.builder()
                .id((long) BusinessTier.basic.ordinal())
                .name(BusinessTier.basic)
                .build();
        Business business = Business.builder()
                .id(1L)
                .user(user)
                .name("test")
                .verified(false)
                .tier(tier)
                .build();

        Business savedBusiness = businessRepository.save(business);

        assertThat(savedBusiness).isNotNull();
        assertThat(savedBusiness.getUser().getId()).isEqualTo(business.getUser().getId());
        assertThat(savedBusiness.getTier().getId()).isEqualTo(business.getTier().getId());
        assertThat(savedBusiness.getName()).isEqualTo(business.getName());
        assertThat(savedBusiness.getVerified()).isEqualTo(business.getVerified());
    }

    @Test
    void shouldGetAllBusiness() {
        int nUsers = 10;

        for (int i = 0; i < nUsers; i++) {
            utils.createBusiness(null);
        }

        List<Business> business = businessRepository.findAll();

        assertThat(business.size()).isEqualTo(nUsers);
    }

    @Test
    void shouldCheckThatBusinessExistsById() {
        Business business = utils.createBusiness(null);

        boolean expected = businessRepository.existsById(business.getId());
        Optional<Business> expectedBusiness = businessRepository.findById(business.getId());

        assertThat(expected).isTrue();
        assertThat(expectedBusiness.isPresent()).isTrue();
        assertThat(expectedBusiness.get().getUser().getId()).isEqualTo(business.getUser().getId());
        assertThat(expectedBusiness.get().getTier().getId()).isEqualTo(business.getTier().getId());
        assertThat(expectedBusiness.get().getName()).isEqualTo(business.getName());
        assertThat(expectedBusiness.get().getVerified()).isEqualTo(business.getVerified());
    }

    @Test
    void shouldCheckThatBusinessDoesNotExistsById() {
        boolean expected = businessRepository.existsById(1L);
        Optional<Business> expectedBusiness = businessRepository.findById(1L);

        assertThat(expected).isFalse();
        assertThat(expectedBusiness.isEmpty()).isTrue();
    }

    @Test
    void shouldDeleteBusiness() {
        Business business = utils.createBusiness(null);

        businessRepository.delete(business);

        List<Business> businessList = businessRepository.findAll();
        assertThat(businessList.size()).isEqualTo(0);
    }

    @Test
    void shouldCheckThatBusinessExistsByUserId() {
        User user = utils.createUser();
        Business business = utils.createBusiness(user);

        boolean expected = businessRepository.existsByUserId(user.getId());
        Optional<Business> expectedBusiness = businessRepository.findByUserId(user.getId());

        assertThat(expected).isTrue();
        assertThat(expectedBusiness.isPresent()).isTrue();
        assertThat(expectedBusiness.get().getId()).isEqualTo(business.getId());
        assertThat(expectedBusiness.get().getName()).isEqualTo(business.getName());
        assertThat(expectedBusiness.get().getVerified()).isEqualTo(business.getVerified());
        assertThat(expectedBusiness.get().getTier()).isEqualTo(business.getTier());
    }

    @Test
    void shouldCheckThatBusinessDoesNotExistsByUserId() {
        User user = utils.createUser();
        utils.createBusiness(user);

        boolean expected = businessRepository.existsByUserId(user.getId() + 1);
        Optional<Business> expectedBusiness = businessRepository.findByUserId(user.getId() + 1);

        assertThat(expected).isFalse();
        assertThat(expectedBusiness.isEmpty()).isTrue();
    }

    @Test
    void shouldCheckThatBusinessExistsByUserEmail() {
        User user = utils.createUser();
        Business business = utils.createBusiness(user);

        boolean expected = businessRepository.existsByUserEmail(user.getEmail());
        Optional<Business> expectedBusiness = businessRepository.findByUserEmail(user.getEmail());

        assertThat(expected).isTrue();
        assertThat(expectedBusiness.isPresent()).isTrue();
        assertThat(expectedBusiness.get().getId()).isEqualTo(business.getId());
        assertThat(expectedBusiness.get().getName()).isEqualTo(business.getName());
        assertThat(expectedBusiness.get().getName()).isEqualTo(business.getName());
        assertThat(expectedBusiness.get().getVerified()).isEqualTo(business.getVerified());
        assertThat(expectedBusiness.get().getTier()).isEqualTo(business.getTier());
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