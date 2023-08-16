package com.paca.paca.reservation;

import com.paca.paca.PacaTest;
import com.paca.paca.utils.TestUtils;
import com.paca.paca.branch.model.Branch;
import com.paca.paca.reservation.model.Reservation;
import com.paca.paca.user.repository.RoleRepository;
import com.paca.paca.user.repository.UserRepository;
import com.paca.paca.branch.repository.BranchRepository;
import com.paca.paca.branch.repository.AmenityRepository;
import com.paca.paca.business.repository.BusinessRepository;
import com.paca.paca.reservation.repository.GuestRepository;
import com.paca.paca.reservation.statics.ReservationStatics;
import com.paca.paca.branch.repository.BranchAmenityRepository;
import com.paca.paca.reservation.repository.ReservationRepository;
import com.paca.paca.reservation.repository.ClientGroupRepository;

import org.junit.jupiter.api.*;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.concurrent.ThreadLocalRandom;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@Testcontainers
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ReservationRepositoryTest extends PacaTest {

    @Autowired
    private ClientGroupRepository clientGroupRepository;

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
                .amenityRepository(amenityRepository)
                .clientGroupRepository(clientGroupRepository)
                .build();
    }

    @BeforeEach
    void restoreReservationDB() {
        clientGroupRepository.deleteAll();
        reservationRepository.deleteAll();
        guestRepository.deleteAll();
    }

    @AfterEach
    void restoreTest() {
        clientGroupRepository.deleteAll();
        guestRepository.deleteAll();
        reservationRepository.deleteAll();
        branchRepository.deleteAll();
        amenityRepository.deleteAll();
        businessRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    void shouldGetReservationsByBranchIdAndFilters() {
        Random rand = new Random();

        // Create random branches
        List<Branch> branches = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            branches.add(utils.createBranch(null));
        }

        // Create random names
        List<String> names = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            names.add("Test name " + i);
        }

        // Create random surnames
        List<String> surnames = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            surnames.add("Test surname " + i);
        }

        // Create random identity documents
        List<String> identityDocuments = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            identityDocuments.add("Test identity document " + i);
        }

        // Min and max dates
        Date minDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(minDate);
        calendar.add(Calendar.MONTH, 1);
        Date maxDate = calendar.getTime();

        List<Reservation> reservations = utils.createTestReservations(
                branches,
                minDate,
                maxDate,
                names,
                surnames,
                identityDocuments);

        // Select a random values
        Branch branch = branches.get(rand.nextInt(branches.size()));
        Date startDate = new Date(ThreadLocalRandom.current().nextLong(
                minDate.getTime(),
                maxDate.getTime()));
        Date endDate = new Date(ThreadLocalRandom.current().nextLong(
                startDate.getTime(),
                maxDate.getTime()));
        Short status = ReservationStatics.Status.ALL.get(rand.nextInt(ReservationStatics.Status.ALL.size()));
        String name = names.get(rand.nextInt(names.size()));
        String surname = surnames.get(rand.nextInt(surnames.size()));
        String identityDocument = identityDocuments.get(rand.nextInt(identityDocuments.size()));

        // Filter the reservations
        List<Reservation> expected = reservations.stream()
                .filter(reservation -> reservation.getBranch().getId().equals(branch.getId()))
                .filter(reservation -> reservation.getReservationDateIn().compareTo(startDate) >= 0)
                .filter(reservation -> reservation.getReservationDateIn().compareTo(endDate) <= 0)
                .filter(reservation -> reservation.getStatus().equals(status))
                .filter(reservation -> reservation.getGuest().getName().contains(name))
                .filter(reservation -> reservation.getGuest().getSurname().contains(surname))
                .filter(reservation -> reservation.getGuest().getIdentityDocument().contains(identityDocument))
                .collect(Collectors.toList());

        // Get the reservations from the repository
        List<Reservation> response = reservationRepository.findAllByBranchIdAndFilters(
                branch.getId(),
                List.of(status),
                startDate,
                endDate,
                name,
                surname,
                identityDocument);

        assertThat(response.size()).isEqualTo(expected.size());
    }

    @Test
    void shouldCheckThatReservationExistsByIdAndBranchBusinessId() {
        Branch branch = utils.createBranch(null);
        Reservation reservation = utils.createReservation(branch, null);

        boolean exists = reservationRepository.existsByIdAndBranch_Business_Id(
                reservation.getId(),
                branch.getBusiness().getId());

        assertThat(exists).isTrue();
    }

    @Test
    void shouldCheckThatReservationDoesNotExistsByIdAndBranchBusinessId() {
        Branch branch = utils.createBranch(null);
        Reservation reservation = utils.createReservation(branch, null);

        boolean exists = reservationRepository.existsByIdAndBranch_Business_Id(
                reservation.getId(),
                branch.getBusiness().getId() + 1);

        assertThat(exists).isFalse();
    }

    @Test
    void shouldCheckThatReservationExistsByBranchBusinessIdAndGuestId() {
        Branch branch = utils.createBranch(null);
        Reservation reservation = utils.createReservation(branch, null);

        boolean exists = reservationRepository.existsByBranchBusinessIdAndGuestId(
                branch.getBusiness().getId(),
                reservation.getGuest().getId());

        assertThat(exists).isTrue();
    }

    @Test
    void shouldCheckThatReservationDoesNotExistsByBranchBusinessIdAndGuestId() {
        Branch branch = utils.createBranch(null);
        Reservation reservation = utils.createReservation(branch, null);

        boolean exists = reservationRepository.existsByBranchBusinessIdAndGuestId(
                branch.getBusiness().getId() + 1,
                reservation.getGuest().getId());

        assertThat(exists).isFalse();
    }
}