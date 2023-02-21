package com.paca.paca.client;

import org.junit.Assert;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import junit.framework.TestCase;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import com.paca.paca.utils.TestUtils;
import com.paca.paca.user.model.User;
import com.paca.paca.client.model.Client;
import com.paca.paca.client.model.Friend;
import com.paca.paca.client.dto.ClientDTO;
import com.paca.paca.client.dto.FriendDTO;
import com.paca.paca.client.dto.ClientListDTO;
import com.paca.paca.client.utils.ClientMapper;
import com.paca.paca.client.utils.FriendMapper;
import com.paca.paca.client.service.ClientService;
import com.paca.paca.reservation.model.ClientGroup;
import com.paca.paca.user.repository.UserRepository;
import com.paca.paca.client.repository.ClientRepository;
import com.paca.paca.client.repository.FriendRepository;
import com.paca.paca.reservation.dto.ReservationListDTO;
import com.paca.paca.exception.exceptions.ConflictException;
import com.paca.paca.exception.exceptions.NoContentException;
import com.paca.paca.reservation.repository.ClientGroupRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {
    
    @Mock
    private ClientRepository clientRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FriendRepository friendRepository;

    @Mock
    private ClientGroupRepository clientGroupRepository;

    @Mock
    private ClientMapper clientMapper;

    @Mock
    private FriendMapper friendMapper;

    @InjectMocks
    private ClientService clientService;

    private TestUtils utils = TestUtils.builder().build();

    @Test 
    void shouldGetAllClients() {
        List<Client> clients = TestUtils.castList(Client.class, Mockito.mock(List.class));

        when(clientRepository.findAll()).thenReturn(clients);
        ClientListDTO responseDTO = clientService.getAll();

        assertThat(responseDTO).isNotNull();
    }

    @Test 
    void shouldGetNoContentDueToMissingClientInGetClientById() {
        when(clientRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            clientService.getById(1L);
            TestCase.fail();
        } catch (Exception e){
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "Client with id 1 does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 28);
        }
    }

    @Test 
    void shouldGetClientById() {
        Client client = utils.createClient(null);
        ClientDTO dto = utils.createClientDTO(client);

        when(clientRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(client));
        when(clientMapper.toDTO(any(Client.class))).thenReturn(dto);

        ClientDTO dtoResponse = clientService.getById(client.getId());

        assertThat(dtoResponse).isNotNull();
        assertThat(dtoResponse.getId()).isEqualTo(client.getId());
        assertThat(dtoResponse.getUserId()).isEqualTo(client.getUser().getId());
    }

    @Test
    void shouldGetNoContentDueToMissingUserInSave() {
        ClientDTO dto = utils.createClientDTO(null);

        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.empty());

        try {
            clientService.save(dto);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "User with email " + dto.getEmail() + " does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 30);
        }
    }

    @Test
    void shouldGetClientDueToExistingClientInSave() {
        Client client = utils.createClient(null);
        ClientDTO dto = utils.createClientDTO(client);

        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.ofNullable(client.getUser()));
        when(clientRepository.existsByUserEmail(any(String.class))).thenReturn(true);

        try {
            clientService.save(dto);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof ConflictException);
            Assert.assertEquals(e.getMessage(), "Client with email " + dto.getEmail() + " already exists");
            Assert.assertEquals(((ConflictException) e).getCode(), (Integer) 12);
        }
    }
    
    @Test
    void shouldSave() {
        Client client = utils.createClient(null);
        ClientDTO dto = utils.createClientDTO(client);

        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.ofNullable(client.getUser()));
        when(clientRepository.existsByUserEmail(any(String.class))).thenReturn(false);
        when(clientRepository.save(any(Client.class))).thenReturn(client);
        when(clientMapper.toEntity(any(ClientDTO.class), any(User.class))).thenReturn(client);
        when(clientMapper.toDTO(any(Client.class))).thenReturn(dto);

        ClientDTO dtoResponse = clientService.save(dto);

        assertThat(dtoResponse).isNotNull();
        assertThat(dtoResponse.getId()).isEqualTo(client.getId());
        assertThat(dtoResponse.getUserId()).isEqualTo(client.getUser().getId());
        assertThat(dtoResponse.getEmail()).isEqualTo(client.getUser().getEmail());
    }

    @Test
    void shouldGetNoContentDueToMissingClientInUpdate() {
        Client client = utils.createClient(null);
        ClientDTO dto = utils.createClientDTO(client);

        when(clientRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            clientService.update(client.getId(), dto);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "Client with id " + client.getId() + " does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 28);
        }
    }

    @Test
    void shouldUpdate() {
        Client client = utils.createClient(null);
        ClientDTO dto = utils.createClientDTO(client);

        when(clientRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(client));
        when(clientRepository.save(any(Client.class))).thenReturn(client);
        when(clientMapper.updateModel(any(ClientDTO.class), any(Client.class))).thenReturn(client);
        when(clientMapper.toDTO(any(Client.class))).thenReturn(dto);

        ClientDTO dtoResponse = clientService.update(client.getId(), dto);

        assertThat(dtoResponse).isNotNull();
        assertThat(dtoResponse.getId()).isEqualTo(client.getId());
        assertThat(dtoResponse.getUserId()).isEqualTo(client.getUser().getId());
    }

    @Test
    void shouldGetNoContentDueToMissingClientInDelete() {
        Client client = utils.createClient(null);

        when(clientRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            clientService.delete(client.getId());
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "Client with id " + client.getId() + " does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 28);
        }
    }

    @Test 
    void shouldGetNoContentDueToMissingClientInGetClientByUserId() {
        Client client = utils.createClient(null);

        when(clientRepository.findByUserId(any(Long.class))).thenReturn(Optional.empty());

        try {
            clientService.getByUserId(client.getUser().getId());
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "User with id " + client.getUser().getId() + " does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 12);
        }
    }

    @Test 
    void shouldGetClientByUserId() {
        Client client = utils.createClient(null);
        ClientDTO dto = utils.createClientDTO(client);

        when(clientRepository.findByUserId(any(Long.class))).thenReturn(Optional.ofNullable(client));
        when(clientMapper.toDTO(any(Client.class))).thenReturn(dto);

        ClientDTO dtoResponse = clientService.getByUserId(client.getUser().getId());

        assertThat(dtoResponse).isNotNull();
        assertThat(dtoResponse.getId()).isEqualTo(client.getId());
        assertThat(dtoResponse.getUserId()).isEqualTo(client.getUser().getId());
    }

    @Test 
    void shouldGetNoContentDueToMissingClientInGetPendingRequestById() {
        Client client = utils.createClient(null);

        when(clientRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            clientService.getPendingFriends(client.getId());
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "Client with id " + client.getId() + " does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 28);
        }
    }

    @Test
    void shouldGetPendingRequestById() {
        Client client = utils.createClient(null);
        List<Friend> requests = TestUtils.castList(
                Friend.class,
                Mockito.mock(List.class));

        when(clientRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(client));
        when(friendRepository.findAllByAddresserIdAndAcceptedFalseAndRejectedFalse(any(Long.class)))
                .thenReturn(requests);

        ClientListDTO responseDTO = clientService.getPendingFriends(client.getId());

        assertThat(responseDTO).isNotNull();
    }

    @Test 
    void shouldGetNoContentDueToMissingClientInGetAcceptedRequestById() {
        Client client = utils.createClient(null);

        when(clientRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            clientService.getAcceptedFriends(client.getId());
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "Client with id " + client.getId() + " does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 28);
        }
    }

    @Test
    void shouldGetAcceptedRequestById() {
        Client client = utils.createClient(null);
        List<Friend> requests = TestUtils.castList(
                Friend.class,
                Mockito.mock(List.class));

        when(clientRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(client));
        when(friendRepository.findAllByAddresserIdAndAcceptedTrue(any(Long.class)))
                .thenReturn(requests);
        when(friendRepository.findAllByRequesterIdAndAcceptedTrue(any(Long.class)))
                .thenReturn(requests);

        ClientListDTO responseDTO = clientService.getAcceptedFriends(client.getId());

        assertThat(responseDTO).isNotNull();
    }

    @Test 
    void shouldGetNoContentDueToMissingClientInGetRejectedRequestById() {
        Client client = utils.createClient(null);

        when(clientRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            clientService.getRejectedFriends(client.getId());
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "Client with id " + client.getId() + " does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 28);
        }
    }

    @Test
    void shouldGetRejectedRequestById() {
        Client client = utils.createClient(null);
        List<Friend> requests = TestUtils.castList(
                Friend.class,
                Mockito.mock(List.class));

        when(clientRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(client));
        when(friendRepository.findAllByAddresserIdAndRejectedTrue(any(Long.class)))
                .thenReturn(requests);

        ClientListDTO responseDTO = clientService.getRejectedFriends(client.getId());

        assertThat(responseDTO).isNotNull();
    }

    @Test
    void shouldGetConflictDueToRequesterIdEqualToAddresserIdInCreateFriendRequest() {
        Client client = utils.createClient(null);

        try {
            clientService.friendRequest(client.getId(), client.getId());
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof ConflictException);
            Assert.assertEquals(e.getMessage(), "Requester and addresser can not be the same");
            Assert.assertEquals(((ConflictException) e).getCode(), (Integer) 28);
        }
    }

    @Test
    void shouldGetNoContentDueToMissingRequestInCreateFriendRequest() {
        Client client = utils.createClient(null);
        when(clientRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            clientService.friendRequest(client.getId(), client.getId() + 1);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "Requester with id: " + client.getId() + " does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 13);
        }
    }

    @Test
    void shouldGetConflictDueToFriendRequestAlreadyExistsInCreateFriendRequest() {
        Client client = utils.createClient(null);

        when(clientRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(client));
        when(friendRepository.existsByRequesterIdAndAddresserId(
                any(Long.class),
                any(Long.class))).thenReturn(true);

        try {
            clientService.friendRequest(client.getId(), client.getId() + 1);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof ConflictException);
            Assert.assertEquals(e.getMessage(), "Friend request already exists");
            Assert.assertEquals(((ConflictException) e).getCode(), (Integer) 15);
        }
    }

    @Test
    void shouldCreateFriendRequest() {
        Friend request = utils.createFriendRequest(null, null, false, false);
        FriendDTO dto = utils.createFriendRequestDTO(request);

        when(clientRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(request.getRequester()));
        when(friendRepository.existsByRequesterIdAndAddresserId(
                any(Long.class),
                any(Long.class))).thenReturn(false);
        when(friendRepository.save(any(Friend.class)))
                .thenReturn(request);
        when(friendMapper.toDTO(any(Friend.class)))
                .thenReturn(dto);

        FriendDTO responseDTO = clientService.friendRequest(
                request.getRequester().getId(),
                request.getAddresser().getId());

        assertThat(responseDTO).isNotNull();
    }

    @Test
    void shouldGetNoContentDueToMissingFriendRequestInAcceptFriendRequest() {
        Client client = utils.createClient(null);

        when(friendRepository.findByRequesterIdAndAddresserId(
                any(Long.class), 
                any(Long.class))).thenReturn(Optional.empty());

        try {
            clientService.acceptFriendRequest(client.getId(), client.getId());
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "Friend request does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 16);
        }
    }

    @Test
    void shouldGetConflictDueToFriendRequestAlreadyAcceptedInAcceptFriendRequest() {
        Friend request = utils.createFriendRequest(null, null, true, false);

        when(friendRepository.findByRequesterIdAndAddresserId(
                any(Long.class),
                any(Long.class))).thenReturn(Optional.ofNullable(request));

        try {
            clientService.acceptFriendRequest(
                    request.getRequester().getId(),
                    request.getAddresser().getId());
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof ConflictException);
            Assert.assertEquals(e.getMessage(), "Friend request already accepted");
            Assert.assertEquals(((ConflictException) e).getCode(), (Integer) 17);
        }
    }
    
    @Test
    void shouldGetConflictDueToFriendRequestAlreadyRejectedInAcceptFriendRequest() {
        Friend request = utils.createFriendRequest(null, null, false, true);

        when(friendRepository.findByRequesterIdAndAddresserId(
                any(Long.class),
                any(Long.class))).thenReturn(Optional.ofNullable(request));

        try {
            clientService.acceptFriendRequest(
                    request.getRequester().getId(),
                    request.getAddresser().getId());
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof ConflictException);
            Assert.assertEquals(e.getMessage(), "Friend request already rejected");
            Assert.assertEquals(((ConflictException) e).getCode(), (Integer) 18);
        }
    }

    @Test
    void shouldAcceptFriendRequest() {
        Friend request = utils.createFriendRequest(null, null, false, false);
        FriendDTO dto = utils.createFriendRequestDTO(request);

        when(friendRepository.findByRequesterIdAndAddresserId(
                any(Long.class),
                any(Long.class))).thenReturn(Optional.ofNullable(request));
        when(friendRepository.save(any(Friend.class))).thenReturn(request);
        when(friendMapper.toDTO(any(Friend.class))).thenReturn(dto);

        FriendDTO responseDTO = clientService.acceptFriendRequest(
                request.getRequester().getId(),
                request.getAddresser().getId());

        assertThat(responseDTO).isNotNull();
    }

    @Test
    void shouldGetNoContentDueToMissingFriendRequestInRejectFriendRequest() {
        Client client = utils.createClient(null);

        when(friendRepository.findByRequesterIdAndAddresserId(
                any(Long.class), 
                any(Long.class))).thenReturn(Optional.empty());

        try {
            clientService.rejectFriendRequest(client.getId(), client.getId());
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "Friend request does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 16);
        }
    }

    @Test
    void shouldGetConflictDueToFriendRequestAlreadyAcceptedInRejectFriendRequest() {
        Friend request = utils.createFriendRequest(null, null, true, false);

        when(friendRepository.findByRequesterIdAndAddresserId(
                any(Long.class),
                any(Long.class))).thenReturn(Optional.ofNullable(request));

        try {
            clientService.rejectFriendRequest(
                    request.getRequester().getId(),
                    request.getAddresser().getId());
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof ConflictException);
            Assert.assertEquals(e.getMessage(), "Friend request already accepted");
            Assert.assertEquals(((ConflictException) e).getCode(), (Integer) 17);
        }
    }

    @Test
    void shouldGetConflictDueToFriendRequestAlreadyRejectedInRejectFriendRequest() {
        Friend request = utils.createFriendRequest(null, null, false, true);

        when(friendRepository.findByRequesterIdAndAddresserId(
                any(Long.class),
                any(Long.class))).thenReturn(Optional.ofNullable(request));

        try {
            clientService.acceptFriendRequest(
                    request.getRequester().getId(),
                    request.getAddresser().getId());
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof ConflictException);
            Assert.assertEquals(e.getMessage(), "Friend request already rejected");
            Assert.assertEquals(((ConflictException) e).getCode(), (Integer) 18);
        }
    }

    @Test
    void shouldRejectFriendRequest() {
        Friend request = utils.createFriendRequest(null, null, false, false);
        FriendDTO dto = utils.createFriendRequestDTO(request);

        when(friendRepository.findByRequesterIdAndAddresserId(
                any(Long.class),
                any(Long.class))).thenReturn(Optional.ofNullable(request));
        when(friendRepository.save(any(Friend.class))).thenReturn(request);
        when(friendMapper.toDTO(any(Friend.class))).thenReturn(dto);

        FriendDTO responseDTO = clientService.rejectFriendRequest(
                request.getRequester().getId(),
                request.getAddresser().getId());

        assertThat(responseDTO).isNotNull();
    }

    @Test
    void shouldGetNoContentDueToMissingFriendRequestInDeleteFriendRequest() {
        Client client = utils.createClient(null);

        when(friendRepository.findByRequesterIdAndAddresserId(
                any(Long.class),
                any(Long.class))).thenReturn(Optional.empty());

        try {
            clientService.deleteFriendRequest(client.getId(), client.getId());
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "Friend request does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 16);
        }
    }

    @Test
    void shouldGetNoContentDueToMissingClientInGetAllReservations() {
        Client client = utils.createClient(null);

        when(clientRepository.findById(any(Long.class)))
                .thenReturn(Optional.empty());

        try {
            clientService.getReservations(client.getId());
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "Client with id: " + client.getId() + " does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 28);
        }
    }

    @Test
    void shouldGetAllReservations() {
        Client client = utils.createClient(null);
        List<ClientGroup> clientGroups = TestUtils.castList(
                ClientGroup.class,
                Mockito.mock(List.class));

        when(clientRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(client));
        when(clientGroupRepository.findAllByClientId(any(Long.class)))
                .thenReturn(clientGroups);

        ReservationListDTO response = clientService.getReservations(client.getId());

        assertThat(response).isNotNull();
    }

    @Test
    void shouldGetNoContentDueToMissingClientInGetAllReservationsByDate() {
        Client client = utils.createClient(null);

        when(clientRepository.findById(any(Long.class)))
                .thenReturn(Optional.empty());

        try {
            clientService.getReservationsByDate(client.getId(), new Date(System.currentTimeMillis()));
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "Client with id: " + client.getId() + " does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 28);
        }
    }

    @Test
    void shouldGetAllReservationsByDate() {
        Client client = utils.createClient(null);
        List<ClientGroup> clientGroups = TestUtils.castList(
                ClientGroup.class,
                Mockito.mock(List.class));

        when(clientRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(client));
        when(clientGroupRepository.findAllByClientIdAndReservationReservationDateGreaterThanEqual(
                any(Long.class),
                any(Date.class))).thenReturn(clientGroups);

        ReservationListDTO response = clientService.getReservationsByDate(
            client.getId(),
            new Date(System.currentTimeMillis()));

        assertThat(response).isNotNull();
    }
}
