package com.paca.paca.reservation;

import com.paca.paca.PacaTest;
import com.paca.paca.branch.model.Branch;
import com.paca.paca.branch.repository.AmenityRepository;
import com.paca.paca.branch.repository.BranchAmenityRepository;
import com.paca.paca.branch.repository.BranchRepository;
import com.paca.paca.business.repository.BusinessRepository;
import com.paca.paca.reservation.model.Reservation;
import com.paca.paca.reservation.repository.ReservationRepository;
import com.paca.paca.user.repository.RoleRepository;
import com.paca.paca.user.repository.UserRepository;
import com.paca.paca.utils.TestUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@Testcontainers
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ReservationRepositoryTest extends PacaTest {

    @Autowired
    private ReservationRepository reservationRepository;
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
                .reservationRepository((reservationRepository))
                .build();
    }

    @BeforeEach
    void restoreReservationDB() {
        reservationRepository.deleteAll();
    }

    @AfterEach
    void restoreTest() {
        reservationRepository.deleteAll();
        branchRepository.deleteAll();
        amenityRepository.deleteAll();
        businessRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    void shouldCreateReservation() {
        Branch branch = utils.createBranch(null);
        Reservation reservation = Reservation.builder()
                .id(1L)
                .branch(branch)
                .requestDate(new Date(System.currentTimeMillis()))
                .reservationDate(new Date(System.currentTimeMillis()))
                .clientNumber(1)
                .payment("69")
                .status(1)
                .payDate(new Date(System.currentTimeMillis()))
                .price(6.9F)
                .occasion("Anniversary")
                .petition("Candles")
                .byClient(Boolean.TRUE)
                .build();

        Reservation savedReservation = reservationRepository.save(reservation);

        assertThat(savedReservation).isNotNull();
        assertThat(savedReservation.getBranch().getId()).isEqualTo(reservation.getBranch().getId());
        assertThat(savedReservation.getRequestDate()).isEqualTo(reservation.getRequestDate());
        assertThat(savedReservation.getReservationDate()).isEqualTo(reservation.getReservationDate());
        assertThat(savedReservation.getClientNumber()).isEqualTo(reservation.getClientNumber());
        assertThat(savedReservation.getPayment()).isEqualTo(reservation.getPayment());
        assertThat(savedReservation.getStatus()).isEqualTo(reservation.getStatus());
        assertThat(savedReservation.getPayDate()).isEqualTo(reservation.getPayDate());
        assertThat(savedReservation.getPrice()).isEqualTo(reservation.getPrice());
        assertThat(savedReservation.getOccasion()).isEqualTo(reservation.getOccasion());
        assertThat(savedReservation.getPetition()).isEqualTo(reservation.getPetition());
        assertThat(savedReservation.getByClient()).isEqualTo(reservation.getByClient());
    }

    @Test
    void shouldGetAllReservations() {
        int nUsers = 10;

        for (int i = 0; i < nUsers; i++) {
            utils.createReservation(null);
        }

        List<Reservation> reservations = reservationRepository.findAll();

        assertThat(reservations.size()).isEqualTo(nUsers);
    }

    @Test
    void shouldCheckThatReservationExistsById() {
        Reservation reservation = utils.createReservation(null);

        boolean expected = reservationRepository.existsById(reservation.getId());
        Optional<Reservation> expectedReservation = reservationRepository.findById(reservation.getId());

        assertThat(expected).isTrue();
        assertThat(expectedReservation.isPresent()).isTrue();
        assertThat(expectedReservation.get().getBranch().getId()).isEqualTo(reservation.getBranch().getId());
        assertThat(expectedReservation.get().getRequestDate()).isEqualTo(reservation.getRequestDate());
        assertThat(expectedReservation.get().getReservationDate()).isEqualTo(reservation.getReservationDate());
        assertThat(expectedReservation.get().getClientNumber()).isEqualTo(reservation.getClientNumber());
        assertThat(expectedReservation.get().getPayment()).isEqualTo(reservation.getPayment());
        assertThat(expectedReservation.get().getStatus()).isEqualTo(reservation.getStatus());
        assertThat(expectedReservation.get().getPayDate()).isEqualTo(reservation.getPayDate());
        assertThat(expectedReservation.get().getPrice()).isEqualTo(reservation.getPrice());
        assertThat(expectedReservation.get().getOccasion()).isEqualTo(reservation.getOccasion());
        assertThat(expectedReservation.get().getPetition()).isEqualTo(reservation.getPetition());
        assertThat(expectedReservation.get().getByClient()).isEqualTo(reservation.getByClient());
    }

    @Test
    void shouldCheckThatReservationDoesNotExistsById() {
        boolean expected = reservationRepository.existsById(1L);
        Optional<Reservation> expectedReservation = reservationRepository.findById(1L);

        assertThat(expected).isFalse();
        assertThat(expectedReservation.isEmpty()).isTrue();
    }

    @Test
    void shouldDeleteReservation() {
        Reservation reservation = utils.createReservation(null);

        reservationRepository.delete(reservation);

        List<Reservation> reservations = reservationRepository.findAll();
        assertThat(reservations.size()).isEqualTo(0);
    }
}