package com.paca.paca.reservation;

import com.paca.paca.PacaTest;
import com.paca.paca.utils.TestUtils;
import com.paca.paca.branch.model.Branch;
import com.paca.paca.reservation.model.Guest;
import com.paca.paca.reservation.model.Reservation;
import com.paca.paca.user.repository.RoleRepository;
import com.paca.paca.user.repository.UserRepository;
import com.paca.paca.branch.repository.BranchRepository;
import com.paca.paca.branch.repository.AmenityRepository;
import com.paca.paca.business.repository.BusinessRepository;
import com.paca.paca.reservation.repository.GuestRepository;
import com.paca.paca.branch.repository.BranchAmenityRepository;
import com.paca.paca.reservation.repository.ReservationRepository;

import org.junit.jupiter.api.*;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.util.Date;
import java.util.List;
import java.util.Calendar;
import java.util.Optional;
import java.math.BigDecimal;
import java.util.stream.Collectors;

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
    private GuestRepository guestRepository;

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
                .guestRepository(guestRepository)
                .reservationRepository((reservationRepository))
                .build();
    }

    @BeforeEach
    void restoreReservationDB() {
        reservationRepository.deleteAll();
        guestRepository.deleteAll();
    }

    @AfterEach
    void restoreTest() {
        guestRepository.deleteAll();
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
        Guest guest = utils.createGuest();

        Reservation reservation = Reservation.builder()
                .id(1L)
                .branch(branch)
                .guest(guest)
                .requestDate(new Date(System.currentTimeMillis()))
                .reservationDateIn(new Date(System.currentTimeMillis()))
                .reservationDateOut(new Date(System.currentTimeMillis()))
                .clientNumber(4)
                .tableNumber(1)
                .payment("69")
                .status(1)
                .payDate(new Date(System.currentTimeMillis()))
                .price(BigDecimal.valueOf(6.9F))
                .occasion("Anniversary")
                .byClient(Boolean.TRUE)
                .build();

        Reservation savedReservation = reservationRepository.save(reservation);

        assertThat(savedReservation).isNotNull();
        assertThat(savedReservation.getBranch().getId()).isEqualTo(reservation.getBranch().getId());
        assertThat(savedReservation.getGuest().getId()).isEqualTo(reservation.getGuest().getId());
        assertThat(new Date(savedReservation.getRequestDate().getTime())).isEqualTo(reservation.getRequestDate());
        assertThat(new Date(savedReservation.getReservationDateIn().getTime()))
                .isEqualTo(reservation.getReservationDateIn());
        assertThat(new Date(savedReservation.getReservationDateOut().getTime()))
                .isEqualTo(reservation.getReservationDateOut());
        assertThat(savedReservation.getClientNumber()).isEqualTo(reservation.getClientNumber());
        assertThat(savedReservation.getTableNumber()).isEqualTo(reservation.getTableNumber());
        assertThat(savedReservation.getPayment()).isEqualTo(reservation.getPayment());
        assertThat(savedReservation.getStatus()).isEqualTo(reservation.getStatus());
        assertThat(new Date(savedReservation.getPayDate().getTime())).isEqualTo(reservation.getPayDate());
        assertThat(savedReservation.getPrice()).isEqualTo(reservation.getPrice());
        assertThat(savedReservation.getOccasion()).isEqualTo(reservation.getOccasion());
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
        Reservation reservation = utils.createReservation(null, null);

        boolean expected = reservationRepository.existsById(reservation.getId());
        Optional<Reservation> expectedReservation = reservationRepository.findById(reservation.getId());

        assertThat(expected).isTrue();
        assertThat(expectedReservation.isPresent()).isTrue();
        assertThat(expectedReservation.get().getBranch().getId()).isEqualTo(reservation.getBranch().getId());
        assertThat(expectedReservation.get().getGuest().getId()).isEqualTo(reservation.getGuest().getId());
        assertThat(expectedReservation.get().getRequestDate()).isEqualTo(reservation.getRequestDate());
        assertThat(expectedReservation.get().getReservationDateIn()).isEqualTo(reservation.getReservationDateIn());
        assertThat(expectedReservation.get().getReservationDateOut()).isEqualTo(reservation.getReservationDateOut());
        assertThat(expectedReservation.get().getClientNumber()).isEqualTo(reservation.getClientNumber());
        assertThat(expectedReservation.get().getTableNumber()).isEqualTo(reservation.getTableNumber());
        assertThat(expectedReservation.get().getPayment()).isEqualTo(reservation.getPayment());
        assertThat(expectedReservation.get().getStatus()).isEqualTo(reservation.getStatus());
        assertThat(expectedReservation.get().getPayDate()).isEqualTo(reservation.getPayDate());
        assertThat(expectedReservation.get().getPrice()).isEqualTo(reservation.getPrice());
        assertThat(expectedReservation.get().getOccasion()).isEqualTo(reservation.getOccasion());
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
        Reservation reservation = utils.createReservation(null, null);

        reservationRepository.delete(reservation);

        List<Reservation> reservations = reservationRepository.findAll();
        assertThat(reservations.size()).isEqualTo(0);
    }

    @Test
    void shouldGetReservationPageByDate() {
        // Create a branch
        Branch branch_1 = utils.createBranch(null);
        Branch branch_2 = utils.createBranch(null);

        // Create two dates
        Calendar calendar = Calendar.getInstance();
        calendar.set(2019, 11, 3, 0, 0, 0);
        Date date_1 = calendar.getTime();

        calendar.set(2019, 11, 4, 0, 0, 0);
        Date date_2 = calendar.getTime();

        List<Reservation> test_reservations = utils.createTestReservations(branch_1, branch_2, date_1, date_2);

        // from the test_reservations look for the ones that belong to branch_1 and
        // date_1

        List<Reservation> filtered_reservations = test_reservations.stream()
                .filter(reservation -> reservation.getBranch().getId().equals(branch_1.getId()))
                .filter(reservation -> reservation.getReservationDateIn().compareTo(date_1) == 0)
                .collect(Collectors.toList());

        // Order the filtered_reservations by reservation_date in descending order
        filtered_reservations.sort((r1, r2) -> r2.getReservationDateIn().compareTo(r1.getReservationDateIn()));

        // Create the page
        Page<Reservation> reservations_page = new PageImpl<>(filtered_reservations);

        Pageable paging = PageRequest.of(
                0,
                20,
                Sort.by("reservationDateIn").descending());

        Page<Reservation> reservations = reservationRepository.findAllByBranchIdAndReservationDateInGreaterThanEqual(
                branch_1.getId(),
                date_1,
                paging);

        assertThat(reservations.getContent().size()).isEqualTo(filtered_reservations.size());
        // Check that each element is equal (Do a loop)
        for (int i = 0; i < reservations.getContent().size(); i++) {
            assertThat(reservations.getContent().get(i).getId())
                    .isEqualTo(reservations_page.getContent().get(i).getId());
        }

    }
}