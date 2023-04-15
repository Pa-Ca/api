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
import com.paca.paca.client.model.Review;
import com.paca.paca.branch.model.Branch;
import com.paca.paca.client.dto.ClientDTO;
import com.paca.paca.client.dto.FriendDTO;
import com.paca.paca.client.dto.ReviewDTO;
import com.paca.paca.client.model.ReviewLike;
import com.paca.paca.client.dto.ReviewListDTO;
import com.paca.paca.client.dto.ClientListDTO;
import com.paca.paca.client.utils.ClientMapper;
import com.paca.paca.client.utils.FriendMapper;
import com.paca.paca.client.utils.ReviewMapper;
import com.paca.paca.client.service.ClientService;
import com.paca.paca.client.service.ReviewService;
import com.paca.paca.reservation.model.ClientGroup;
import com.paca.paca.user.repository.UserRepository;
import com.paca.paca.client.repository.ClientRepository;
import com.paca.paca.branch.repository.BranchRepository;
import com.paca.paca.client.repository.FriendRepository;
import com.paca.paca.client.repository.ReviewRepository;
import com.paca.paca.reservation.dto.ReservationListDTO;
import com.paca.paca.client.repository.ReviewLikeRepository;
import com.paca.paca.exception.exceptions.ConflictException;
import com.paca.paca.exception.exceptions.NoContentException;
import com.paca.paca.exception.exceptions.UnprocessableException;
import com.paca.paca.reservation.repository.ClientGroupRepository;


import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;




import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {

    @Mock
    private UserRepository userRepository;
    
    @Mock
    private ClientRepository clientRepository;

    @Mock
    private BranchRepository branchRepository;

    @Mock
    private FriendRepository friendRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ReviewLikeRepository reviewLikeRepository;

    @Mock
    private ClientGroupRepository clientGroupRepository;

    @Mock
    private ClientMapper clientMapper;

    @Mock
    private FriendMapper friendMapper;

    @Mock
    private ReviewMapper reviewMapper;

    @InjectMocks
    private ClientService clientService;

    @InjectMocks
    private ReviewService reviewService;

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

    @Test
    void shouldGettAllReviews() {
        List<Review> reviews = TestUtils.castList(Review.class, Mockito.mock(List.class));

        when(reviewRepository.findAll()).thenReturn(reviews);
        ReviewListDTO responseDTO = reviewService.getAll();

        assertThat(responseDTO).isNotNull();
    }

    @Test 
    void shouldGetNoContentDueToMissingReviewInGetReviewById() {
        when(reviewRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            reviewService.getById(1L);
            TestCase.fail();
        } catch (Exception e){
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "Review with id 1 does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 35);
        }
    }

    @Test 
    void shouldGetReviewById() {
        Review review = utils.createReview(null, null);
        ReviewDTO dto = utils.createReviewDTO(review);

        when(reviewRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(review));
        when(reviewMapper.toDTO(any(Review.class))).thenReturn(dto);

        ReviewDTO dtoResponse = reviewService.getById(review.getId());

        assertThat(dtoResponse).isNotNull();
        assertThat(dtoResponse.getId()).isEqualTo(review.getId());
        assertThat(dtoResponse.getClientId()).isEqualTo(review.getClient().getId());
        assertThat(dtoResponse.getBranchId()).isEqualTo(review.getBranch().getId());
    }

    @Test 
    void shouldGetNoContentDueToMissingReviewInGetReviewByClientIdAndBranchId() {
        when(reviewRepository.findByClientIdAndBranchId(any(Long.class), any(Long.class))).thenReturn(Optional.empty());

        try {
            reviewService.getByClientIdAndBranchId(1L, 1L);
            TestCase.fail();
        } catch (Exception e){
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "Review does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 35);
        }
    }

    @Test 
    void shouldGetReviewByClientIdAndBranchId() {
        Review review = utils.createReview(null, null);
        ReviewDTO dto = utils.createReviewDTO(review);

        when(reviewRepository.findByClientIdAndBranchId(any(Long.class), any(Long.class)))
                .thenReturn(Optional.ofNullable(review));
        when(reviewMapper.toDTO(any(Review.class))).thenReturn(dto);

        ReviewDTO dtoResponse = reviewService.getByClientIdAndBranchId(
                review.getClient().getId(),
                review.getBranch().getId());

        assertThat(dtoResponse).isNotNull();
        assertThat(dtoResponse.getId()).isEqualTo(review.getId());
        assertThat(dtoResponse.getClientId()).isEqualTo(review.getClient().getId());
        assertThat(dtoResponse.getBranchId()).isEqualTo(review.getBranch().getId());
    }

    @Test
    void shouldGetNoContentDueToMissingClientInSaveReview() {
        Review review = utils.createReview(null, null);
        ReviewDTO dto = utils.createReviewDTO(review);

        when(clientRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            reviewService.save(dto);
            TestCase.fail();
        } catch (Exception e){
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "Client with id " + dto.getClientId() + " does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 28);
        }
    }

    @Test
    void shouldGetNoContentDueToMissingBranchInSaveReview() {
        Review review = utils.createReview(null, null);
        ReviewDTO dto = utils.createReviewDTO(review);

        when(clientRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(review.getClient()));
        when(branchRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            reviewService.save(dto);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "Branch with id " + dto.getBranchId() + " does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 20);
        }
    }
    
    @Test
    void shouldGetConflictDueToReviewAlreadyExistsInSaveReview() {
        Review review = utils.createReview(null, null);
        ReviewDTO dto = utils.createReviewDTO(review);

        when(clientRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(review.getClient()));
        when(branchRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(review.getBranch()));
        when(reviewRepository.findByClientIdAndBranchId(any(Long.class), any(Long.class)))
                .thenReturn(Optional.ofNullable(review));

        try {
            reviewService.save(dto);
            TestCase.fail();
        } catch (Exception e){
            Assert.assertTrue(e instanceof ConflictException);
            Assert.assertEquals(e.getMessage(), "Review already exists");
            Assert.assertEquals(((ConflictException) e).getCode(), (Integer) 39);
        }
    }

    @Test
    void shouldSaveReview() {
        Review review = utils.createReview(null, null);
        ReviewDTO dto = utils.createReviewDTO(review);

        when(clientRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(review.getClient()));
        when(branchRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(review.getBranch()));
        when(reviewRepository.findByClientIdAndBranchId(any(Long.class), any(Long.class)))
                .thenReturn(Optional.empty());
        when(reviewRepository.save(any(Review.class))).thenReturn(review);
        when(reviewMapper.toEntity(any(ReviewDTO.class), any(Client.class), any(Branch.class))).thenReturn(review);
        when(reviewMapper.toDTO(any(Review.class))).thenReturn(dto);

        ReviewDTO dtoResponse = reviewService.save(dto);

        assertThat(dtoResponse).isNotNull();
        assertThat(dtoResponse.getId()).isEqualTo(review.getId());
        assertThat(dtoResponse.getClientId()).isEqualTo(review.getClient().getId());
        assertThat(dtoResponse.getBranchId()).isEqualTo(review.getBranch().getId());
    }

    @Test
    void shouldGetNoContentDueToMissingReviewInUpdateReview() {
        Review review = utils.createReview(null, null);
        ReviewDTO dto = utils.createReviewDTO(review);

        when(reviewRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            reviewService.update(review.getId(), dto);
            TestCase.fail();
        } catch (Exception e){
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "Review with id " + review.getId() + " does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 35);
        }
    }

    @Test 
    void shouldUpdateReview() {
        Review review = utils.createReview(null, null);
        ReviewDTO dto = utils.createReviewDTO(review);

        when(reviewRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(review));
        when(reviewRepository.save(any(Review.class))).thenReturn(review);
        when(reviewMapper.updateModel(any(ReviewDTO.class), any(Review.class))).thenReturn(review);
        when(reviewMapper.toDTO(any(Review.class))).thenReturn(dto);

        ReviewDTO dtoResponse = reviewService.update(review.getId(), dto);

        assertThat(dtoResponse).isNotNull();
        assertThat(dtoResponse.getId()).isEqualTo(review.getId());
        assertThat(dtoResponse.getClientId()).isEqualTo(review.getClient().getId());
        assertThat(dtoResponse.getBranchId()).isEqualTo(review.getBranch().getId());
    }

    @Test
    void shouldGetNoContentDueToMissingReviewInDeleteReview() {
        Review review = utils.createReview(null, null);

        when(reviewRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            reviewService.delete(review.getId());
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "Review with id " + review.getId() + " does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 35);
        }
    }

    @Test
    void shouldGetNoContentDueToMissingReviewInSetLikeToReview() {
        ReviewLike like = utils.createReviewLike(null, null);

        when(reviewRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            reviewService.like(like.getReview().getId(), like.getClient().getId());
            TestCase.fail();
        } catch (Exception e){
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "Review with id " + like.getReview().getId() + " does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 35);
        }
    }
    
    @Test
    void shouldGetNoContentDueToMissingClientInSetLikeToReview() {
        ReviewLike like = utils.createReviewLike(null, null);

        when(reviewRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(like.getReview()));
        when(clientRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            reviewService.like(like.getReview().getId(), like.getClient().getId());
            TestCase.fail();
        } catch (Exception e){
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "Client with id " + like.getClient().getId() + " does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 28);
        }
    }

    @Test
    void shouldGetConflictDueToReviewLikeAlreadyExistsInSetLikeToReview() {
        ReviewLike like = utils.createReviewLike(null, null);

        when(clientRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(like.getClient()));
        when(reviewRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(like.getReview()));
        when(reviewLikeRepository.existsByClientIdAndReviewId(any(Long.class), any(Long.class))).thenReturn(true);

        try {
            reviewService.like(like.getReview().getId(), like.getClient().getId());
            TestCase.fail();
        } catch (Exception e){
            Assert.assertTrue(e instanceof ConflictException);
            Assert.assertEquals(e.getMessage(), "Review like already exists");
            Assert.assertEquals(((ConflictException) e).getCode(), (Integer) 37);
        }
    }

    @Test
    void shouldSetLikeToReview() {
        ReviewLike like = utils.createReviewLike(null, null);
        ReviewDTO dto = utils.createReviewDTO(like.getReview());
        List<ReviewLike> likes = TestUtils.castList(ReviewLike.class, Mockito.mock(List.class));
        likes.add(like);

        when(clientRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(like.getClient()));
        when(reviewRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(like.getReview()));
        when(reviewLikeRepository.existsByClientIdAndReviewId(any(Long.class), any(Long.class))).thenReturn(false);
        when(reviewLikeRepository.save(any(ReviewLike.class))).thenReturn(like);
        when(reviewMapper.toDTO(any(Review.class))).thenReturn(dto);
        when(reviewLikeRepository.findAllByReviewId(any(Long.class))).thenReturn(likes);

        ReviewDTO dtoResponse = reviewService.like(like.getReview().getId(), like.getClient().getId());

        assertThat(dtoResponse).isNotNull();
        assertThat(dtoResponse.getId()).isEqualTo(like.getReview().getId());
        assertThat(dtoResponse.getClientId()).isEqualTo(like.getReview().getClient().getId());
        assertThat(dtoResponse.getLikes()).isEqualTo(1);
    }

    @Test
    void shouldGetNoContentDueToMissingReviewInSetDislikeToReview() {
        ReviewLike like = utils.createReviewLike(null, null);

        when(reviewRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            reviewService.dislike(like.getReview().getId(), like.getClient().getId());
            TestCase.fail();
        } catch (Exception e){
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "Review with id " + like.getReview().getId() + " does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 35);
        }
    }
    
    @Test
    void shouldGetNoContentDueToMissingClientInSetDislikeToReview() {
        ReviewLike like = utils.createReviewLike(null, null);

        when(reviewRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(like.getReview()));
        when(clientRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            reviewService.dislike(like.getReview().getId(), like.getClient().getId());
            TestCase.fail();
        } catch (Exception e){
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "Client with id " + like.getClient().getId() + " does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 28);
        }
    }

    @Test
    void shouldGetNoContentDueToMissingReviewLikeDoesInSetDislikeToReview() {
        ReviewLike like = utils.createReviewLike(null, null);

        when(clientRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(like.getClient()));
        when(reviewRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(like.getReview()));
        when(reviewLikeRepository.findByClientIdAndReviewId(any(Long.class), any(Long.class)))
                .thenReturn(Optional.empty());

        try {
            reviewService.dislike(like.getReview().getId(), like.getClient().getId());
            TestCase.fail();
        } catch (Exception e){
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "Review like does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 38);
        }
    }

    @Test
    void shouldSetDislikeToReview() {
        ReviewLike like = utils.createReviewLike(null, null);
        ReviewDTO dto = utils.createReviewDTO(like.getReview());
        List<ReviewLike> likes = TestUtils.castList(ReviewLike.class, Mockito.mock(List.class));
        likes.add(like);

        when(clientRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(like.getClient()));
        when(reviewRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(like.getReview()));
        when(reviewLikeRepository.findByClientIdAndReviewId(any(Long.class), any(Long.class)))
                .thenReturn(Optional.ofNullable(like));
        when(reviewMapper.toDTO(any(Review.class))).thenReturn(dto);
        when(reviewLikeRepository.findAllByReviewId(any(Long.class))).thenReturn(likes);

        ReviewDTO dtoResponse = reviewService.dislike(like.getReview().getId(), like.getClient().getId());

        assertThat(dtoResponse).isNotNull();
        assertThat(dtoResponse.getId()).isEqualTo(like.getReview().getId());
        assertThat(dtoResponse.getClientId()).isEqualTo(like.getReview().getClient().getId());
        assertThat(dtoResponse.getLikes()).isEqualTo(1);
    }

    // Test for getPage method
    // Lets test the exception when the page is less than 0
    @Test
    void shouldGetUnprocessableDueToPageLessThanZeroInGetPage() {
        try {
            reviewService.getPage(-1, 10);
            TestCase.fail();
        } catch (Exception e){
            Assert.assertTrue(e instanceof UnprocessableException);
            Assert.assertEquals(e.getMessage(), "Page number cannot be less than zero");
            Assert.assertEquals(((UnprocessableException) e).getCode(), (Integer) 40);
        }
    }
    // Lets test the exception when the size is less than 1
    @Test
    void shouldGetUnprocessableDueToSizeLessThanOneInGetPage() {
        try {
            reviewService.getPage(1, 0);
            TestCase.fail();
        } catch (Exception e){
            Assert.assertTrue(e instanceof UnprocessableException);
            Assert.assertEquals(e.getMessage(), "Size cannot be less than one");
            Assert.assertEquals(((UnprocessableException) e).getCode(), (Integer) 41);
        }
    }

    // Now lets test the that the getPage method works as expected
    // First lets create 20 reviews
    // Then lets get the first page with 10 reviews
    // Then lets get the second page with 10 reviews
    // Check if the first page has 10 reviews
    // Check if the second page has 10 reviews
    @Test
    void shouldGetPage() {
        //Page<Review> pagedResult = TestUtils.castPage(Review.class, Mockito.mock(Page.class));

        Pageable pageable = Mockito.mock(Pageable.class);
        when(pageable.getPageSize()).thenReturn(10);
        //when(pageable.getPageNumber()).thenReturn(1);
        

        // Create 20 reviews manually
        List<Review> reviews  = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            reviews.add(utils.createReview(null, null));
        }

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), reviews.size());
        Page<Review> pagedResult = new PageImpl<>(reviews.subList(start, end), pageable, reviews.size());

        // print the pagedResult
        System.out.println("pagedResult: " + pagedResult); 


        //when(reviewRepository.findAll(any(Pageable.class))).thenReturn(pagedResult);
        // when(pagedResult.getContent()).thenReturn(reviews.subList(0, 10));
        // when(pagedResult.hasNext()).thenReturn(true);
        // when(pagedResult.hasPrevious()).thenReturn(false);
        // when(pagedResult.getTotalPages()).thenReturn(2);
        // when(pagedResult.getTotalElements()).thenReturn(20L);
        // when(pagedResult.getNumber()).thenReturn(0);
        // when(pagedResult.getSize()).thenReturn(10);


        
        when(reviewRepository.findAll(any(Pageable.class))).thenReturn(pagedResult);
        when(reviewMapper.toDTO(any(Review.class))).thenReturn(utils.createReviewDTO(null));

        ReviewListDTO pageResponse = reviewService.getPage(0, 10);
        ReviewListDTO pageResponse2 = reviewService.getPage(1, 10);

        assertThat(pageResponse).isNotNull();
        assertThat(pageResponse2).isNotNull();

        assertThat(pageResponse.getReviews().size()).isEqualTo(10);
        assertThat(pageResponse2.getReviews().size()).isEqualTo(10);

    }




}
