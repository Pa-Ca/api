package com.paca.paca.client;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;

import com.paca.paca.PacaTest;
import com.paca.paca.user.model.User;
import com.paca.paca.utils.TestUtils;
import com.paca.paca.client.model.Client;
import com.paca.paca.client.model.Friend;
import com.paca.paca.user.repository.RoleRepository;
import com.paca.paca.user.repository.UserRepository;
import com.paca.paca.client.repository.ClientRepository;
import com.paca.paca.client.repository.FriendRepository;

import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.util.List;
import java.util.Optional;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@Testcontainers
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ClientRepositoryTest extends PacaTest {
    
    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private FriendRepository friendRepository;

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
                .clientRepository(clientRepository)
                .friendRepository(friendRepository)
                .build();
    }

    @BeforeEach
    void restoreClientDB() {
        clientRepository.deleteAll();
    }

    @AfterEach
    void restoreTest() {
        clientRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    void shouldCreateClient() throws ParseException {
        User user = utils.createUser();
        Client client = Client.builder()
                .id(1L)
                .user(user)
                .name("test")
                .surname("Test")
                .stripeCustomerId("stripe_id_test")
                .phoneNumber("+580000000")
                .address("Test address")
                .dateOfBirth(new SimpleDateFormat("yyyy-MM-dd").parse("2000-01-01"))
                .build();

        Client savedClient = clientRepository.save(client);

        assertThat(savedClient).isNotNull();
        assertThat(savedClient.getUser().getId()).isEqualTo(client.getUser().getId());
        assertThat(savedClient.getName()).isEqualTo(client.getName());
        assertThat(savedClient.getSurname()).isEqualTo(client.getSurname());
        assertThat(savedClient.getStripeCustomerId()).isEqualTo(client.getStripeCustomerId());
        assertThat(savedClient.getPhoneNumber()).isEqualTo(client.getPhoneNumber());
        assertThat(savedClient.getAddress()).isEqualTo(client.getAddress());
        assertThat(savedClient.getDateOfBirth()).isEqualTo(client.getDateOfBirth());
    }

    @Test
    void shouldGetAllClients() throws ParseException {
        int nUsers = 10;

        for (int i = 0; i < nUsers; i++) {
            utils.createClient(null);
        }

        List<Client> clients = clientRepository.findAll();

        assertThat(clients.size()).isEqualTo(nUsers);
    }

    @Test
    void shouldCheckThatClientExistsById() throws ParseException {
        Client client = utils.createClient(null);

        boolean expected = clientRepository.existsById(client.getId());
        Optional<Client> expectedClient = clientRepository.findById(client.getId());

        assertThat(expected).isTrue();
        assertThat(expectedClient.isPresent()).isTrue();
        assertThat(expectedClient.get().getId()).isEqualTo(client.getId());
        assertThat(expectedClient.get().getName()).isEqualTo(client.getName());
        assertThat(expectedClient.get().getSurname()).isEqualTo(client.getSurname());
        assertThat(expectedClient.get().getStripeCustomerId()).isEqualTo(client.getStripeCustomerId());
        assertThat(expectedClient.get().getPhoneNumber()).isEqualTo(client.getPhoneNumber());
        assertThat(expectedClient.get().getAddress()).isEqualTo(client.getAddress());
        assertThat(expectedClient.get().getDateOfBirth()).isEqualTo(client.getDateOfBirth());
    }

    @Test
    void shouldCheckThatClientDoesNotExistsById() {
        boolean expected = clientRepository.existsById(1L);
        Optional<Client> expectedClient = clientRepository.findById(1L);

        assertThat(expected).isFalse();
        assertThat(expectedClient.isEmpty()).isTrue();
    }

    @Test
    void shouldCheckThatClientExistsByUserId() throws ParseException {
        User user = utils.createUser();
        Client client = utils.createClient(user);

        boolean expected = clientRepository.existsByUserId(user.getId());
        Optional<Client> expectedClient = clientRepository.findByUserId(user.getId());

        assertThat(expected).isTrue();
        assertThat(expectedClient.isPresent()).isTrue();
        assertThat(expectedClient.get().getId()).isEqualTo(client.getId());
        assertThat(expectedClient.get().getName()).isEqualTo(client.getName());
        assertThat(expectedClient.get().getSurname()).isEqualTo(client.getSurname());
        assertThat(expectedClient.get().getStripeCustomerId()).isEqualTo(client.getStripeCustomerId());
        assertThat(expectedClient.get().getPhoneNumber()).isEqualTo(client.getPhoneNumber());
        assertThat(expectedClient.get().getAddress()).isEqualTo(client.getAddress());
        assertThat(expectedClient.get().getDateOfBirth()).isEqualTo(client.getDateOfBirth());
    }

    @Test
    void shouldCheckThatClientDoesNotExistsByUserId() throws ParseException {
        User user = utils.createUser();
        utils.createClient(user);

        boolean expected = clientRepository.existsByUserId(user.getId() + 1);
        Optional<Client> expectedClient = clientRepository.findByUserId(user.getId() + 1);

        assertThat(expected).isFalse();
        assertThat(expectedClient.isEmpty()).isTrue();
    }
    
    @Test
    void shouldCheckThatClientExistsByUserEmail() throws ParseException {
        User user = utils.createUser();
        Client client = utils.createClient(user);

        boolean expected = clientRepository.existsByUserEmail(user.getEmail());
        Optional<Client> expectedClient = clientRepository.findByUserEmail(user.getEmail());

        assertThat(expected).isTrue();
        assertThat(expectedClient.isPresent()).isTrue();
        assertThat(expectedClient.get().getId()).isEqualTo(client.getId());
        assertThat(expectedClient.get().getName()).isEqualTo(client.getName());
        assertThat(expectedClient.get().getSurname()).isEqualTo(client.getSurname());
        assertThat(expectedClient.get().getStripeCustomerId()).isEqualTo(client.getStripeCustomerId());
        assertThat(expectedClient.get().getPhoneNumber()).isEqualTo(client.getPhoneNumber());
        assertThat(expectedClient.get().getAddress()).isEqualTo(client.getAddress());
        assertThat(expectedClient.get().getDateOfBirth()).isEqualTo(client.getDateOfBirth());
    }

    @Test
    void shouldCheckThatClientDoesNotExistsByUserEmail() throws ParseException {
        User user = utils.createUser();
        utils.createClient(user);

        boolean expected = clientRepository.existsByUserEmail(user.getEmail() + "a");
        Optional<Client> expectedClient = clientRepository.findByUserEmail(user.getEmail() + "a");

        assertThat(expected).isFalse();
        assertThat(expectedClient.isEmpty()).isTrue();
    }

    @Test
    void shouldDeleteClient() throws ParseException {
        Client client = utils.createClient(null);
        
        clientRepository.delete(client);

        List<Client> clients = clientRepository.findAll();
        assertThat(clients.size()).isEqualTo(0);
    }

    @Test 
    void shouldCheckThatFriendRequestExistsById() throws ParseException {
        Friend request = utils.createFriendRequest(null, null, false, false);

        boolean expected = friendRepository.existsById(request.getId());
        Optional<Friend> expectedRequest = friendRepository.findById(request.getId());

        assertThat(expected).isTrue();
        assertThat(expectedRequest.isPresent()).isTrue();
        assertThat(expectedRequest.get().getId()).isEqualTo(request.getId());
        assertThat(expectedRequest.get().getRequester().getId()).isEqualTo(request.getRequester().getId());
        assertThat(expectedRequest.get().getAddresser().getId()).isEqualTo(request.getAddresser().getId());
        assertThat(expectedRequest.get().getAccepted()).isEqualTo(request.getAccepted());
        assertThat(expectedRequest.get().getRejected()).isEqualTo(request.getRejected());
    }

    @Test
    void shouldCheckThatFriendRequestDoesNotExistsById() throws ParseException {
        Friend request = utils.createFriendRequest(null, null, false, false);
        
        boolean expected = friendRepository.existsById(request.getId() + 1);
        Optional<Friend> expectedRequest = friendRepository.findById(request.getId() + 1);

        assertThat(expected).isFalse();
        assertThat(expectedRequest.isEmpty()).isTrue();
    }

    @Test 
    void shouldCheckThatFriendRequestExistsByRequesterIdAndAddresserId() throws ParseException {
        Friend request = utils.createFriendRequest(null, null, false, false);

        boolean expected = friendRepository.existsByRequesterIdAndAddresserId(
                request.getRequester().getId(),
                request.getAddresser().getId());
        Optional<Friend> expectedRequest = friendRepository.findByRequesterIdAndAddresserId(
                request.getRequester().getId(),
                request.getAddresser().getId());

        assertThat(expected).isTrue();
        assertThat(expectedRequest.isPresent()).isTrue();
        assertThat(expectedRequest.get().getId()).isEqualTo(request.getId());
        assertThat(expectedRequest.get().getRequester().getId()).isEqualTo(request.getRequester().getId());
        assertThat(expectedRequest.get().getAddresser().getId()).isEqualTo(request.getAddresser().getId());
        assertThat(expectedRequest.get().getAccepted()).isEqualTo(request.getAccepted());
        assertThat(expectedRequest.get().getRejected()).isEqualTo(request.getRejected());
    }

    @Test 
    void shouldCheckThatFriendRequestDoesNotExistsByRequesterIdAndAddresserId() throws ParseException {
        Client requester = utils.createClient(null);
        Client addresser = utils.createClient(null);
        utils.createFriendRequest(requester, addresser, false, false);

        boolean expected = friendRepository.existsByRequesterIdAndAddresserId(
                requester.getId() + 1,
                addresser.getId() + 1);
        Optional<Friend> expectedRequest = friendRepository.findByRequesterIdAndAddresserId(
                requester.getId() + 1,
                addresser.getId() + 1);

        assertThat(expected).isFalse();
        assertThat(expectedRequest.isEmpty()).isTrue();

        expected = friendRepository.existsByRequesterIdAndAddresserId(
                requester.getId() + 1,
                addresser.getId());
        expectedRequest = friendRepository.findByRequesterIdAndAddresserId(
                requester.getId() + 1,
                addresser.getId());

        assertThat(expected).isFalse();
        assertThat(expectedRequest.isEmpty()).isTrue();

        expected = friendRepository.existsByRequesterIdAndAddresserId(
                requester.getId(),
                addresser.getId() + 1);
        expectedRequest = friendRepository.findByRequesterIdAndAddresserId(
                requester.getId(),
                addresser.getId() + 1);

        assertThat(expected).isFalse();
        assertThat(expectedRequest.isEmpty()).isTrue();
    }

    @Test 
    void shouldGetAllFriendRequestsExistsByRequesterIdAndAcceptedTrue() throws ParseException {
        Client requester1 = utils.createClient(null);
        Friend request1 = utils.createFriendRequest(requester1, null, true, false);
        Friend request2 = utils.createFriendRequest(requester1, null, true, false);

        utils.createFriendRequest(requester1, null, false, false);
        Client requester4 = utils.createClient(null);
        utils.createFriendRequest(requester4, null, true, false);
        Client requester5 = utils.createClient(null);
        utils.createFriendRequest(requester5, null, false, false);

        List<Friend> requests = friendRepository.findAllByRequesterIdAndAcceptedTrue(requester1.getId());

        assertThat(requests.size()).isEqualTo(2);
        assertThat(requests.contains(request1)).isTrue();
        assertThat(requests.contains(request2)).isTrue();
    }

    @Test 
    void shouldGetAllFriendRequestsExistsByAddresserIdAndAcceptedTrue() throws ParseException {
        Client addresser1 = utils.createClient(null);
        Friend request1 = utils.createFriendRequest(null, addresser1, true, false);
        Friend request2 = utils.createFriendRequest(null, addresser1, true, false);

        utils.createFriendRequest(null, addresser1, false, false);
        Client addresser4 = utils.createClient(null);
        utils.createFriendRequest(null, addresser4, true, false);
        Client addresser5 = utils.createClient(null);
        utils.createFriendRequest(null, addresser5, false, false);

        List<Friend> requests = friendRepository.findAllByAddresserIdAndAcceptedTrue(addresser1.getId());

        assertThat(requests.size()).isEqualTo(2);
        assertThat(requests.contains(request1)).isTrue();
        assertThat(requests.contains(request2)).isTrue();
    }

    @Test 
    void shouldGetAllFriendRequestsExistsByRequesterIdAndRejectedTrue() throws ParseException {
        Client requester1 = utils.createClient(null);
        Friend request1 = utils.createFriendRequest(requester1, null, false, true);
        Friend request2 = utils.createFriendRequest(requester1, null, false, true);

        utils.createFriendRequest(requester1, null, false, false);
        Client requester4 = utils.createClient(null);
        utils.createFriendRequest(requester4, null, false, true);
        Client requester5 = utils.createClient(null);
        utils.createFriendRequest(requester5, null, false, false);

        List<Friend> requests = friendRepository.findAllByRequesterIdAndRejectedTrue(requester1.getId());

        assertThat(requests.size()).isEqualTo(2);
        assertThat(requests.contains(request1)).isTrue();
        assertThat(requests.contains(request2)).isTrue();
    }

    @Test 
    void shouldGetAllFriendRequestsExistsByAddresserIdAndRejectedTrue() throws ParseException {
        Client addresser1 = utils.createClient(null);
        Friend request1 = utils.createFriendRequest(null, addresser1, false, true);
        Friend request2 = utils.createFriendRequest(null, addresser1, false, true);

        utils.createFriendRequest(null, addresser1, false, false);
        Client addresser4 = utils.createClient(null);
        utils.createFriendRequest(null, addresser4, false, true);
        Client addresser5 = utils.createClient(null);
        utils.createFriendRequest(null, addresser5, false, false);

        List<Friend> requests = friendRepository.findAllByAddresserIdAndRejectedTrue(addresser1.getId());

        assertThat(requests.size()).isEqualTo(2);
        assertThat(requests.contains(request1)).isTrue();
        assertThat(requests.contains(request2)).isTrue();
    }

    @Test 
    void shouldGetAllFriendRequestsExistsByRequesterIdAndAcceptedFalseAndRejectedFalse() throws ParseException {
        Client requester1 = utils.createClient(null);
        Friend request1 = utils.createFriendRequest(requester1, null, false, false);
        Friend request2 = utils.createFriendRequest(requester1, null, false, false);

        utils.createFriendRequest(requester1, null, true, false);
        utils.createFriendRequest(requester1, null, false, true);
        Client requester4 = utils.createClient(null);
        utils.createFriendRequest(requester4, null, false, false);
        Client requester5 = utils.createClient(null);
        utils.createFriendRequest(requester5, null, true, false);

        List<Friend> requests = friendRepository
                .findAllByRequesterIdAndAcceptedFalseAndRejectedFalse(requester1.getId());

        assertThat(requests.size()).isEqualTo(2);
        assertThat(requests.contains(request1)).isTrue();
        assertThat(requests.contains(request2)).isTrue();
    }
    
    @Test 
    void shouldGetAllFriendRequestsExistsByAddresserIdAndAcceptedFalseAndRejectedFalse() throws ParseException {
        Client addresser1 = utils.createClient(null);
        Friend request1 = utils.createFriendRequest(null, addresser1, false, false);
        Friend request2 = utils.createFriendRequest(null, addresser1, false, false);

        utils.createFriendRequest(null, addresser1, true, false);
        utils.createFriendRequest(null, addresser1, false, true);
        Client addresser4 = utils.createClient(null);
        utils.createFriendRequest(null, addresser4, false, false);
        Client addresser5 = utils.createClient(null);
        utils.createFriendRequest(null, addresser5, true, false);

        List<Friend> requests = friendRepository
                .findAllByAddresserIdAndAcceptedFalseAndRejectedFalse(addresser1.getId());

        assertThat(requests.size()).isEqualTo(2);
        assertThat(requests.contains(request1)).isTrue();
        assertThat(requests.contains(request2)).isTrue();
    }
}
