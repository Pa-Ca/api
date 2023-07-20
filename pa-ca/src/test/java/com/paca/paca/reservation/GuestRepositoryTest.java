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

import java.util.List;
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
    void shouldCreateGuest() {
        Guest guest = Guest.builder()
                .id(1L)
                .name("name_test")
                .surname("surname_test")
                .email("email_test")
                .phoneNumber("phone_number_test")
                .identityDocument("iden_doc_test")
                .build();

        Guest savedGuest = guestRepository.save(guest);

        assertThat(savedGuest).isNotNull();
        assertThat(savedGuest.getName()).isEqualTo(guest.getName());
        assertThat(savedGuest.getSurname()).isEqualTo(guest.getSurname());
        assertThat(savedGuest.getEmail()).isEqualTo(guest.getEmail());
        assertThat(savedGuest.getPhoneNumber()).isEqualTo(guest.getPhoneNumber());
        assertThat(savedGuest.getIdentityDocument()).isEqualTo(guest.getIdentityDocument());
    }

    @Test
    void shouldGetAllGuests() {
        int nUsers = 10;

        for (int i = 0; i < nUsers; i++) {
            utils.createGuest();
        }

        List<Guest> guests = guestRepository.findAll();

        assertThat(guests.size()).isEqualTo(nUsers);
    }

    @Test
    void shouldCheckThatGuestExistsById() {
        Guest guest = utils.createGuest();

        boolean expected = guestRepository.existsById(guest.getId());
        Optional<Guest> expectedGuest = guestRepository.findById(guest.getId());

        assertThat(expected).isTrue();
        assertThat(expectedGuest.isPresent()).isTrue();
        assertThat(expectedGuest.get().getName()).isEqualTo(guest.getName());
        assertThat(expectedGuest.get().getSurname()).isEqualTo(guest.getSurname());
        assertThat(expectedGuest.get().getEmail()).isEqualTo(guest.getEmail());
        assertThat(expectedGuest.get().getPhoneNumber()).isEqualTo(guest.getPhoneNumber());
        assertThat(expectedGuest.get().getIdentityDocument()).isEqualTo(guest.getIdentityDocument());
    }

    @Test
    void shouldCheckThatGuestDoesNotExistsById() {
        boolean expected = guestRepository.existsById(1L);
        Optional<Guest> expectedGuest = guestRepository.findById(1L);

        assertThat(expected).isFalse();
        assertThat(expectedGuest.isEmpty()).isTrue();
    }

    @Test
    void shouldDeleteGuest() {
        Guest guest = utils.createGuest();

        guestRepository.delete(guest);

        List<Guest> guests = guestRepository.findAll();
        assertThat(guests.size()).isEqualTo(0);
    }
}