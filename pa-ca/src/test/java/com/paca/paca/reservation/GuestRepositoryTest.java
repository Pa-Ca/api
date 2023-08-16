package com.paca.paca.reservation;

import com.paca.paca.PacaTest;
import com.paca.paca.utils.TestUtils;
import com.paca.paca.reservation.model.Guest;
import com.paca.paca.user.repository.RoleRepository;
import com.paca.paca.user.repository.UserRepository;
import com.paca.paca.branch.repository.BranchRepository;
import com.paca.paca.branch.repository.AmenityRepository;
import com.paca.paca.business.repository.BusinessRepository;
import com.paca.paca.reservation.repository.GuestRepository;
import com.paca.paca.branch.repository.BranchAmenityRepository;

import org.junit.jupiter.api.*;
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
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class GuestRepositoryTest extends PacaTest {

    @Autowired
    private GuestRepository guestRepository;

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private AmenityRepository amenityRepository;

    @Autowired
    private BranchAmenityRepository branchAmenityRepository;

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    private TestUtils utils;

    @BeforeAll
    void initUtils() {
        utils = TestUtils.builder()
                .roleRepository(roleRepository)
                .userRepository(userRepository)
                .businessRepository(businessRepository)
                .branchAmenityRepository(branchAmenityRepository)
                .branchRepository(branchRepository)
                .guestRepository((guestRepository))
                .build();
    }

    @BeforeEach
    void restoreGuestDB() {
        guestRepository.deleteAll();
    }

    @AfterEach
    void restoreTest() {
        guestRepository.deleteAll();
        branchRepository.deleteAll();
        amenityRepository.deleteAll();
        businessRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    void shouldCheckThatGuestExistsByIdentityDocument() {
        Guest guest = utils.createGuest();

        Optional<Guest> expectedGuest = guestRepository.findByIdentityDocument(guest.getIdentityDocument());

        assertThat(expectedGuest.isPresent()).isTrue();
        assertThat(expectedGuest.get()).isEqualTo(guest);
    }

    @Test
    void shouldCheckThatGuestDoesNotExistsByIdentityDocument() {
        Optional<Guest> expectedGuest = guestRepository.findByIdentityDocument("V");

        assertThat(expectedGuest.isEmpty()).isTrue();
    }

}