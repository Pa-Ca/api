package com.paca.paca.client;

import org.junit.Assert;
import org.mockito.Mockito;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import junit.framework.TestCase;

import com.paca.paca.ServiceTest;
import com.paca.paca.utils.TestUtils;
import com.paca.paca.user.model.User;
import com.paca.paca.client.model.Client;
import com.paca.paca.client.model.Friend;
import com.paca.paca.client.model.Review;
import com.paca.paca.branch.model.Branch;
import com.paca.paca.client.dto.ClientDTO;
import com.paca.paca.client.dto.FriendDTO;
import com.paca.paca.client.dto.ReviewDTO;
import com.paca.paca.branch.dto.BranchDTO;
import com.paca.paca.client.model.ReviewLike;
import com.paca.paca.client.dto.ClientInfoDTO;
import com.paca.paca.client.model.ClientGuest;
import com.paca.paca.branch.dto.BranchListDTO;
import com.paca.paca.client.dto.ClientListDTO;
import com.paca.paca.client.model.FavoriteBranch;
import com.paca.paca.client.service.ClientService;
import com.paca.paca.client.service.ReviewService;
import com.paca.paca.reservation.model.ClientGroup;
import com.paca.paca.reservation.dto.ReservationInfoListDTO;
import com.paca.paca.exception.exceptions.ConflictException;
import com.paca.paca.exception.exceptions.NotFoundException;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ClientServiceTest extends ServiceTest {

    @InjectMocks
    private ClientService clientService;

    @InjectMocks
    private ReviewService reviewService;

    @Test 
    void shouldGetNotFoundDueToMissingClientInGetClientById() {
        when(clientRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            clientService.getById(1L);
            TestCase.fail();
        } catch (Exception e){
            Assert.assertTrue(e instanceof NotFoundException);
            Assert.assertEquals(e.getMessage(), "Client with id 1 does not exists");
            Assert.assertEquals(((NotFoundException) e).getCode(), (Integer) 28);
        }
    }

    @Test
    void shouldGetClientById() {
        Client client = utils.createClient(null);
        ClientDTO dto = utils.createClientDTO(client);

        when(clientRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(client));
        when(clientMapper.toDTO(any(Client.class))).thenReturn(dto);

        ClientDTO response = clientService.getById(client.getId());

        assertThat(response).isEqualTo(dto);
    }

    @Test
    void shouldGetNotFoundDueToMissingUserInSave() {
        ClientDTO dto = utils.createClientDTO(null);

        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.empty());

        try {
            clientService.save(dto);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NotFoundException);
            Assert.assertEquals(e.getMessage(), "User with email " + dto.getEmail() + " does not exists");
            Assert.assertEquals(((NotFoundException) e).getCode(), (Integer) 30);
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
        ClientGuest clientGuest = utils.createClientGuest(client);

        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.ofNullable(client.getUser()));
        when(clientRepository.existsByUserEmail(any(String.class))).thenReturn(false);
        when(clientMapper.toEntity(any(ClientDTO.class), any(User.class))).thenReturn(client);
        when(clientRepository.save(any(Client.class))).thenReturn(client);
        when(clientGuestRepository.save(any(ClientGuest.class))).thenReturn(clientGuest);
        when(clientMapper.toDTO(any(Client.class))).thenReturn(dto);

        ClientInfoDTO response = clientService.save(dto);
        ClientInfoDTO expected = new ClientInfoDTO(dto, clientGuest.getId());

        assertThat(response).isEqualTo(expected);
    }

    @Test
    void shouldGetNotFoundDueToMissingClientInUpdate() {
        Client client = utils.createClient(null);
        ClientDTO dto = utils.createClientDTO(client);

        when(clientRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            clientService.update(client.getId(), dto);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NotFoundException);
            Assert.assertEquals(e.getMessage(), "Client with id " + client.getId() + " does not exists");
            Assert.assertEquals(((NotFoundException) e).getCode(), (Integer) 28);
        }
    }

    @Test
    void shouldUpdate() {
        Client client = utils.createClient(null);
        ClientDTO dto = utils.createClientDTO(client);
        ClientGuest clientGuest = utils.createClientGuest(client);

        when(clientRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(client));
        when(clientRepository.save(any(Client.class))).thenReturn(client);
        when(clientMapper.updateModel(any(ClientDTO.class), any(Client.class))).thenReturn(client);
        when(clientMapper.toDTO(any(Client.class))).thenReturn(dto);
        when(clientGuestRepository.findByClientId(anyLong())).thenReturn(Optional.of(clientGuest));

        ClientInfoDTO response = clientService.update(client.getId(), dto);
        ClientInfoDTO expected = new ClientInfoDTO(dto, clientGuest.getId());

        assertThat(response).isEqualTo(expected);
    }

    @Test
    void shouldGetNotFoundDueToMissingClientInDelete() {
        Client client = utils.createClient(null);

        when(clientRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            clientService.delete(client.getId());
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NotFoundException);
            Assert.assertEquals(e.getMessage(), "Client with id " + client.getId() + " does not exists");
            Assert.assertEquals(((NotFoundException) e).getCode(), (Integer) 28);
        }
    }

    @Test
    void shouldGetNotFoundDueToMissingClientInGetClientByUserId() {
        Client client = utils.createClient(null);

        when(clientRepository.findByUserId(any(Long.class))).thenReturn(Optional.empty());

        try {
            clientService.getByUserId(client.getUser().getId());
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NotFoundException);
            Assert.assertEquals(e.getMessage(), "User with id " + client.getUser().getId() + " does not exists");
            Assert.assertEquals(((NotFoundException) e).getCode(), (Integer) 12);
        }
    }

    @Test
    void shouldGetClientByUserId() {
        Client client = utils.createClient(null);
        ClientDTO dto = utils.createClientDTO(client);
        ClientGuest clientGuest = utils.createClientGuest(client);

        when(clientRepository.findByUserId(any(Long.class))).thenReturn(Optional.ofNullable(client));
        when(clientMapper.toDTO(any(Client.class))).thenReturn(dto);
        when(clientGuestRepository.findByClientId(anyLong())).thenReturn(Optional.of(clientGuest));

        ClientInfoDTO response = clientService.getByUserId(client.getUser().getId());
        ClientInfoDTO expected = new ClientInfoDTO(dto, clientGuest.getId());

        assertThat(response).isEqualTo(expected);
    }

    @Test
    void shouldGetNotFoundDueToMissingClientInGetPendingRequestById() {
        Client client = utils.createClient(null);

        when(clientRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            clientService.getPendingFriends(client.getId());
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NotFoundException);
            Assert.assertEquals(e.getMessage(), "Client with id " + client.getId() + " does not exists");
            Assert.assertEquals(((NotFoundException) e).getCode(), (Integer) 28);
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
    void shouldGetNotFoundDueToMissingClientInGetAcceptedRequestById() {
        Client client = utils.createClient(null);

        when(clientRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            clientService.getAcceptedFriends(client.getId());
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NotFoundException);
            Assert.assertEquals(e.getMessage(), "Client with id " + client.getId() + " does not exists");
            Assert.assertEquals(((NotFoundException) e).getCode(), (Integer) 28);
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
    void shouldGetNotFoundDueToMissingClientInGetRejectedRequestById() {
        Client client = utils.createClient(null);

        when(clientRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            clientService.getRejectedFriends(client.getId());
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NotFoundException);
            Assert.assertEquals(e.getMessage(), "Client with id " + client.getId() + " does not exists");
            Assert.assertEquals(((NotFoundException) e).getCode(), (Integer) 28);
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
    void shouldGetNotFoundDueToMissingRequestInCreateFriendRequest() {
        Client client = utils.createClient(null);
        when(clientRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            clientService.friendRequest(client.getId(), client.getId() + 1);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NotFoundException);
            Assert.assertEquals(e.getMessage(), "Requester with id: " + client.getId() + " does not exists");
            Assert.assertEquals(((NotFoundException) e).getCode(), (Integer) 13);
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

        assertThat(responseDTO).isEqualTo(dto);
    }

    @Test
    void shouldGetNotFoundDueToMissingFriendRequestInAcceptFriendRequest() {
        Client client = utils.createClient(null);

        when(friendRepository.findByRequesterIdAndAddresserId(
                any(Long.class),
                any(Long.class))).thenReturn(Optional.empty());

        try {
            clientService.acceptFriendRequest(client.getId(), client.getId());
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NotFoundException);
            Assert.assertEquals(e.getMessage(), "Friend request does not exists");
            Assert.assertEquals(((NotFoundException) e).getCode(), (Integer) 16);
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

        assertThat(responseDTO).isEqualTo(dto);
    }

    @Test
    void shouldGetNotFoundDueToMissingFriendRequestInRejectFriendRequest() {
        Client client = utils.createClient(null);

        when(friendRepository.findByRequesterIdAndAddresserId(
                any(Long.class),
                any(Long.class))).thenReturn(Optional.empty());

        try {
            clientService.rejectFriendRequest(client.getId(), client.getId());
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NotFoundException);
            Assert.assertEquals(e.getMessage(), "Friend request does not exists");
            Assert.assertEquals(((NotFoundException) e).getCode(), (Integer) 16);
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

        assertThat(responseDTO).isEqualTo(dto);
    }

    @Test
    void shouldGetNotFoundDueToMissingFriendRequestInDeleteFriendRequest() {
        Client client = utils.createClient(null);

        when(friendRepository.findByRequesterIdAndAddresserId(
                any(Long.class),
                any(Long.class))).thenReturn(Optional.empty());

        try {
            clientService.deleteFriendRequest(client.getId(), client.getId());
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NotFoundException);
            Assert.assertEquals(e.getMessage(), "Friend request does not exists");
            Assert.assertEquals(((NotFoundException) e).getCode(), (Integer) 16);
        }
    }

    @Test
    void shouldGetNotFoundDueToMissingClientInGetReservations() {
        Client client = utils.createClient(null);

        when(clientRepository.findById(any(Long.class)))
                .thenReturn(Optional.empty());

        try {
            clientService.getReservations(client.getId());
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NotFoundException);
            Assert.assertEquals(e.getMessage(), "Client with id " + client.getId() + " does not exists");
            Assert.assertEquals(((NotFoundException) e).getCode(), (Integer) 28);
        }
    }

    @Test
    void shouldGetReservations() {
        Client client = utils.createClient(null);
        List<ClientGroup> clientGroups = TestUtils.castList(
                ClientGroup.class,
                Mockito.mock(List.class));

        when(clientRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(client));
        when(clientGroupRepository.findAllByClientId(any(Long.class)))
                .thenReturn(clientGroups);

        ReservationInfoListDTO response = clientService.getReservations(client.getId());

        assertThat(response).isNotNull();
    }

    @Test
    void shouldGetNotFoundDueToMissingClientInGetFavoriteBranchs() {
        Client client = utils.createClient(null);

        when(clientRepository.findById(any(Long.class)))
                .thenReturn(Optional.empty());

        try {
            clientService.getFavoriteBranches(client.getId());
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NotFoundException);
            Assert.assertEquals(e.getMessage(), "Client with id " + client.getId() + " does not exists");
            Assert.assertEquals(((NotFoundException) e).getCode(), (Integer) 28);
        }
    }

    @Test
    void shouldGetFavoriteBranchs() {
        Client client = utils.createClient(null);
        List<FavoriteBranch> favoriteBranchs = TestUtils.castList(
                FavoriteBranch.class,
                Mockito.mock(List.class));

        when(clientRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(client));
        when(favoriteBranchRepository.findAllByClientId(any(Long.class)))
                .thenReturn(favoriteBranchs);

        BranchListDTO response = clientService.getFavoriteBranches(client.getId());

        assertThat(response).isNotNull();
    }

    @Test
    void shouldGetNotFoundDueToMissingClientInAddFavoriteBranch() {
        when(clientRepository.findById(anyLong())).thenReturn(Optional.empty());

        try {
            clientService.addFavoriteBranch(1L, 1L);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NotFoundException);
            Assert.assertEquals(e.getMessage(), "Client with id " + 1L + " does not exists");
            Assert.assertEquals(((NotFoundException) e).getCode(), (Integer) 28);
        }
    }

    @Test
    void shouldGetNotFoundDueToMissingBranchInAddFavoriteBranch() {
        Client client = utils.createClient(null);

        when(clientRepository.findById(anyLong())).thenReturn(Optional.of(client));
        when(branchRepository.findById(anyLong())).thenReturn(Optional.empty());

        try {
            clientService.addFavoriteBranch(client.getId(), 1L);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NotFoundException);
            Assert.assertEquals(e.getMessage(), "Branch with id " + 1L + " does not exists");
            Assert.assertEquals(((NotFoundException) e).getCode(), (Integer) 20);
        }
    }

    @Test
    void shouldGetClientDueToExistingFavoriteBranchInAddFavoriteBranch() {
        Client client = utils.createClient(null);
        Branch branch = utils.createBranch(null);

        when(clientRepository.findById(anyLong())).thenReturn(Optional.of(client));
        when(branchRepository.findById(anyLong())).thenReturn(Optional.of(branch));
        when(favoriteBranchRepository.existsByClientIdAndBranchId(anyLong(), anyLong())).thenReturn(true);

        try {
            clientService.addFavoriteBranch(client.getId(), branch.getId());
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof ConflictException);
            Assert.assertEquals(e.getMessage(), "Favorite branch already exists");
            Assert.assertEquals(((ConflictException) e).getCode(), (Integer) 32);
        }
    }

    @Test
    void shouldAddFavoriteBranch() {
        Client client = utils.createClient(null);
        Branch branch = utils.createBranch(null);
        FavoriteBranch fav = utils.createFavoriteBranch(client, branch);
        BranchDTO branchDTO = utils.createBranchDTO(branch);

        when(clientRepository.findById(anyLong())).thenReturn(Optional.of(client));
        when(branchRepository.findById(anyLong())).thenReturn(Optional.of(branch));
        when(favoriteBranchRepository.existsByClientIdAndBranchId(anyLong(), anyLong())).thenReturn(false);
        when(favoriteBranchRepository.save(any(FavoriteBranch.class))).thenReturn(fav);
        when(branchMapper.toDTO(any(Branch.class))).thenReturn(branchDTO);

        BranchDTO response = clientService.addFavoriteBranch(client.getId(), branch.getId());

        assertThat(response).isEqualTo(branchDTO);
    }

    @Test
    void shouldGetNotFoundDueToMissingClientInDeleteFavoriteBranch() {
        Client client = utils.createClient(null);
        Branch branch = utils.createBranch(null);

        when(clientRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            clientService.deleteFavoriteBranch(client.getId(), branch.getId());
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NotFoundException);
            Assert.assertEquals(e.getMessage(), "Client with id " + client.getId() + " does not exists");
            Assert.assertEquals(((NotFoundException) e).getCode(), (Integer) 28);
        }
    }

    @Test
    void shouldGetNotFoundDueToMissingBranchInDeleteFavoriteBranch() {
        Client client = utils.createClient(null);
        Branch branch = utils.createBranch(null);

        when(clientRepository.findById(any(Long.class))).thenReturn(Optional.of(client));
        when(branchRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            clientService.deleteFavoriteBranch(client.getId(), branch.getId());
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NotFoundException);
            Assert.assertEquals(e.getMessage(), "Branch with id " + client.getId() + " does not exists");
            Assert.assertEquals(((NotFoundException) e).getCode(), (Integer) 20);
        }
    }

    @Test
    void shouldGetNotFoundDueToMissingFavortieBranchInDeleteFavoriteBranch() {
        Client client = utils.createClient(null);
        Branch branch = utils.createBranch(null);

        when(clientRepository.findById(any(Long.class))).thenReturn(Optional.of(client));
        when(branchRepository.findById(any(Long.class))).thenReturn(Optional.of(branch));
        when(favoriteBranchRepository.existsByClientIdAndBranchId(anyLong(), anyLong())).thenReturn(false);

        try {
            clientService.deleteFavoriteBranch(client.getId(), branch.getId());
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NotFoundException);
            Assert.assertEquals(e.getMessage(), "Favorite branch does not exists");
            Assert.assertEquals(((NotFoundException) e).getCode(), (Integer) 33);
        }
    }

    @Test 
    void shouldGetNotFoundDueToMissingReviewInGetReviewById() {
        when(reviewRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            reviewService.getById(1L);
            TestCase.fail();
        } catch (Exception e){
            Assert.assertTrue(e instanceof NotFoundException);
            Assert.assertEquals(e.getMessage(), "Review with id 1 does not exists");
            Assert.assertEquals(((NotFoundException) e).getCode(), (Integer) 35);
        }
    }

    @Test
    void shouldGetReviewById() {
        Review review = utils.createReview(null, null);
        ReviewDTO dto = utils.createReviewDTO(review);

        when(reviewRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(review));
        when(reviewMapper.toDTO(any(Review.class))).thenReturn(dto);

        ReviewDTO response = reviewService.getById(review.getId());

        assertThat(response).isEqualTo(dto);
    }

    @Test
    void shouldGetNotFoundDueToMissingClientInSaveReview() {
        Review review = utils.createReview(null, null);
        ReviewDTO dto = utils.createReviewDTO(review);

        when(clientRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            reviewService.save(dto);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NotFoundException);
            Assert.assertEquals(e.getMessage(), "Client with id " + dto.getClientId() + " does not exists");
            Assert.assertEquals(((NotFoundException) e).getCode(), (Integer) 28);
        }
    }

    @Test
    void shouldGetNotFoundDueToMissingBranchInSaveReview() {
        Review review = utils.createReview(null, null);
        ReviewDTO dto = utils.createReviewDTO(review);

        when(clientRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(review.getClient()));
        when(branchRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            reviewService.save(dto);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NotFoundException);
            Assert.assertEquals(e.getMessage(), "Branch with id " + dto.getBranchId() + " does not exists");
            Assert.assertEquals(((NotFoundException) e).getCode(), (Integer) 20);
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
        } catch (Exception e) {
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

        ReviewDTO response = reviewService.save(dto);

        assertThat(response).isEqualTo(dto);
    }

    @Test
    void shouldGetNotFoundDueToMissingReviewInUpdateReview() {
        Review review = utils.createReview(null, null);
        ReviewDTO dto = utils.createReviewDTO(review);

        when(reviewRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            reviewService.update(review.getId(), dto);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NotFoundException);
            Assert.assertEquals(e.getMessage(), "Review with id " + review.getId() + " does not exists");
            Assert.assertEquals(((NotFoundException) e).getCode(), (Integer) 35);
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

        ReviewDTO response = reviewService.update(review.getId(), dto);

        assertThat(response).isEqualTo(dto);
    }

    @Test
    void shouldGetNotFoundDueToMissingReviewInDeleteReview() {
        Review review = utils.createReview(null, null);

        when(reviewRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            reviewService.delete(review.getId());
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NotFoundException);
            Assert.assertEquals(e.getMessage(), "Review with id " + review.getId() + " does not exists");
            Assert.assertEquals(((NotFoundException) e).getCode(), (Integer) 35);
        }
    }

    @Test
    void shouldGetNotFoundDueToMissingReviewInSetLikeToReview() {
        ReviewLike like = utils.createReviewLike(null, null);

        when(reviewRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            reviewService.like(like.getReview().getId(), like.getClient().getId());
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NotFoundException);
            Assert.assertEquals(e.getMessage(), "Review with id " + like.getReview().getId() + " does not exists");
            Assert.assertEquals(((NotFoundException) e).getCode(), (Integer) 35);
        }
    }

    @Test
    void shouldGetNotFoundDueToMissingClientInSetLikeToReview() {
        ReviewLike like = utils.createReviewLike(null, null);

        when(reviewRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(like.getReview()));
        when(clientRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            reviewService.like(like.getReview().getId(), like.getClient().getId());
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NotFoundException);
            Assert.assertEquals(e.getMessage(), "Client with id " + like.getClient().getId() + " does not exists");
            Assert.assertEquals(((NotFoundException) e).getCode(), (Integer) 28);
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
        } catch (Exception e) {
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

        ReviewDTO response = reviewService.like(like.getReview().getId(), like.getClient().getId());

        assertThat(response).isEqualTo(dto);
    }

    @Test
    void shouldGetNotFoundDueToMissingReviewInSetDislikeToReview() {
        ReviewLike like = utils.createReviewLike(null, null);

        when(reviewRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            reviewService.dislike(like.getReview().getId(), like.getClient().getId());
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NotFoundException);
            Assert.assertEquals(e.getMessage(), "Review with id " + like.getReview().getId() + " does not exists");
            Assert.assertEquals(((NotFoundException) e).getCode(), (Integer) 35);
        }
    }

    @Test
    void shouldGetNotFoundDueToMissingClientInSetDislikeToReview() {
        ReviewLike like = utils.createReviewLike(null, null);

        when(reviewRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(like.getReview()));
        when(clientRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            reviewService.dislike(like.getReview().getId(), like.getClient().getId());
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NotFoundException);
            Assert.assertEquals(e.getMessage(), "Client with id " + like.getClient().getId() + " does not exists");
            Assert.assertEquals(((NotFoundException) e).getCode(), (Integer) 28);
        }
    }

    @Test
    void shouldGetNotFoundDueToMissingReviewLikeDoesInSetDislikeToReview() {
        ReviewLike like = utils.createReviewLike(null, null);

        when(clientRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(like.getClient()));
        when(reviewRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(like.getReview()));
        when(reviewLikeRepository.findByClientIdAndReviewId(any(Long.class), any(Long.class)))
                .thenReturn(Optional.empty());

        try {
            reviewService.dislike(like.getReview().getId(), like.getClient().getId());
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NotFoundException);
            Assert.assertEquals(e.getMessage(), "Review like does not exists");
            Assert.assertEquals(((NotFoundException) e).getCode(), (Integer) 38);
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

        ReviewDTO response = reviewService.dislike(like.getReview().getId(), like.getClient().getId());

        assertThat(response).isEqualTo(dto);
    }

}
