package com.paca.paca.reservation;

import com.paca.paca.PacaTest;
import com.paca.paca.utils.TestUtils;
import com.paca.paca.client.model.Client;
import com.paca.paca.reservation.model.ClientGroup;
import com.paca.paca.reservation.model.Reservation;
import com.paca.paca.user.repository.RoleRepository;
import com.paca.paca.user.repository.UserRepository;
import com.paca.paca.client.repository.ClientRepository;
import com.paca.paca.branch.repository.BranchRepository;
import com.paca.paca.branch.repository.AmenityRepository;
import com.paca.paca.reservation.repository.GuestRepository;
import com.paca.paca.business.repository.BusinessRepository;
import com.paca.paca.branch.repository.BranchAmenityRepository;
import com.paca.paca.reservation.repository.ReservationRepository;
import com.paca.paca.reservation.repository.ClientGroupRepository;

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
class ClientGroupRepositoryTest extends PacaTest {

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

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ClientGroupRepository clientGroupRepository;

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
                .amenityRepository(amenityRepository)
                .clientRepository(clientRepository)
                .reservationRepository(reservationRepository)
                .clientGroupRepository(clientGroupRepository)
                .build();
    }

    @BeforeEach
    void restoreGuestDB() {
        clientGroupRepository.deleteAll();
        reservationRepository.deleteAll();
        guestRepository.deleteAll();
    }

    @AfterEach
    void restoreTest() {
        clientGroupRepository.deleteAll();
        reservationRepository.deleteAll();
        clientRepository.deleteAll();
        guestRepository.deleteAll();
        branchRepository.deleteAll();
        amenityRepository.deleteAll();
        businessRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    void shouldGetAllByClientId() {
        Client client = utils.createClient(null);

        // Create random client groups
        for (int i = 0; i < 10; i++) {
            utils.createClientGroup(client, null);
            utils.createClientGroup(null, null);
        }

        List<ClientGroup> response = clientGroupRepository.findAllByClientId(client.getId());

        assertThat(response.size()).isEqualTo(10);
    }

    @Test
    void shouldGetAllByReservationId() {
        Reservation reservation = utils.createReservation(null);

        // Create random client groups
        for (int i = 0; i < 10; i++) {
            utils.createClientGroup(null, reservation);
            utils.createClientGroup(null, null);
        }

        List<ClientGroup> response = clientGroupRepository.findAllByReservationId(reservation.getId());

        assertThat(response.size()).isEqualTo(10);
    }

    @Test
    void shouldFindByReservationIdAndClientId() {
        Reservation reservation = utils.createReservation(null);
        Client client = utils.createClient(null);
        utils.createClientGroup(client, reservation);

        Optional<ClientGroup> response = clientGroupRepository.findByReservationIdAndClientId(
                reservation.getId(),
                client.getId());

        assertThat(response.isPresent()).isTrue();
    }

}
