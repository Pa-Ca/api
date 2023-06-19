package com.paca.paca.client;

import org.mockito.InjectMocks;
import org.junit.jupiter.api.Test;
import com.paca.paca.user.model.User;
import com.paca.paca.utils.TestUtils;
import com.paca.paca.client.model.Client;
import com.paca.paca.client.model.Friend;
import com.paca.paca.client.model.Review;
import com.paca.paca.client.dto.ClientDTO;
import com.paca.paca.client.dto.FriendDTO;
import com.paca.paca.client.dto.ReviewDTO;
import com.paca.paca.client.utils.ClientMapperImpl;
import com.paca.paca.client.utils.FriendMapperImpl;
import com.paca.paca.client.utils.ReviewMapperImpl;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
public class ClientMapperTest {

    @InjectMocks
    private ClientMapperImpl clientMapper;

    @InjectMocks
    private FriendMapperImpl friendMapper;

    @InjectMocks
    private ReviewMapperImpl reviewMapper;

    private TestUtils utils = TestUtils.builder().build();

    @Test
    void shouldMapClientEntityToClientDTO() {
        Client client = utils.createClient(null);

        ClientDTO response = clientMapper.toDTO(client);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(client.getId());
        assertThat(response.getUserId()).isEqualTo(client.getUser().getId());
        assertThat(response.getName()).isEqualTo(client.getName());
        assertThat(response.getSurname()).isEqualTo(client.getSurname());
        assertThat(response.getStripeCustomerId()).isEqualTo(client.getStripeCustomerId());
        assertThat(response.getPhoneNumber()).isEqualTo(client.getPhoneNumber());
        assertThat(response.getAddress()).isEqualTo(client.getAddress());
        assertThat(response.getDateOfBirth()).isEqualTo(client.getDateOfBirth());
    }

    @Test 
    void shouldMapClientDTOtoClientEntity() {
        User user = utils.createUser();
        ClientDTO dto = utils.createClientDTO(utils.createClient(user));

        Client client = clientMapper.toEntity(dto, user);

        assertThat(client).isNotNull();
        assertThat(client.getId()).isEqualTo(dto.getId());
        assertThat(client.getUser().getId()).isEqualTo(user.getId());
        assertThat(client.getName()).isEqualTo(dto.getName());
        assertThat(client.getSurname()).isEqualTo(dto.getSurname());
        assertThat(client.getStripeCustomerId()).isEqualTo(dto.getStripeCustomerId());
        assertThat(client.getPhoneNumber()).isEqualTo(dto.getPhoneNumber());
        assertThat(client.getAddress()).isEqualTo(dto.getAddress());
        assertThat(client.getDateOfBirth()).isEqualTo(dto.getDateOfBirth());
    }
    
    @Test
    void shouldPartiallyMapClientDTOtoClientEntity() {
        Client client = utils.createClient(null);

        // Not changing ID
        ClientDTO dto = ClientDTO.builder()
                .id(client.getId() + 1)
                .build();
        Client updatedClient = clientMapper.updateModel(dto, client);
        assertThat(updatedClient).isNotNull();
        assertThat(updatedClient.getId()).isEqualTo(client.getId());

        // Not changing User ID
        dto = ClientDTO.builder()
                .userId(client.getUser().getId() + 1)
                .build();
        updatedClient = clientMapper.updateModel(dto, client);
        assertThat(updatedClient).isNotNull();
        assertThat(updatedClient.getUser().getId()).isEqualTo(client.getUser().getId());

        // Changing name
        dto = ClientDTO.builder()
                .name("m_test")
                .build();
        updatedClient = clientMapper.updateModel(dto, client);
        assertThat(updatedClient).isNotNull();
        assertThat(updatedClient.getName()).isEqualTo(dto.getName());

        // Changing surname
        dto = ClientDTO.builder()
                .surname("m_Test")
                .build();
        updatedClient = clientMapper.updateModel(dto, client);
        assertThat(updatedClient).isNotNull();
        assertThat(updatedClient.getSurname()).isEqualTo(dto.getSurname());

        // Change stripe customer id
        dto = ClientDTO.builder()
                .stripeCustomerId("m_stripe_id_test")
                .build();
        updatedClient = clientMapper.updateModel(dto, client);
        assertThat(updatedClient).isNotNull();
        assertThat(updatedClient.getStripeCustomerId()).isEqualTo(dto.getStripeCustomerId());

        // Change phone number
        dto = ClientDTO.builder()
                .phoneNumber("+580000001")
                .build();
        updatedClient = clientMapper.updateModel(dto, client);
        assertThat(updatedClient).isNotNull();
        assertThat(updatedClient.getPhoneNumber()).isEqualTo(dto.getPhoneNumber());

        // Change address
        dto = ClientDTO.builder()
                .address("m_Test address")
                .build();
        updatedClient = clientMapper.updateModel(dto, client);
        assertThat(updatedClient).isNotNull();
        assertThat(updatedClient.getAddress()).isEqualTo(dto.getAddress());

        // Change date of birth
        dto = ClientDTO.builder()
                .dateOfBirth(new Date(System.currentTimeMillis()))
                .build();
        updatedClient = clientMapper.updateModel(dto, client);
        assertThat(updatedClient).isNotNull();
        assertThat(updatedClient.getDateOfBirth()).isEqualTo(dto.getDateOfBirth());
    }

    @Test 
    void shouldMapFriendEntityToFriendDTO() {
        Friend friend = utils.createFriendRequest(null, null, false, false);

        FriendDTO response = friendMapper.toDTO(friend);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(friend.getId());
        assertThat(response.getRequesterId()).isEqualTo(friend.getRequester().getId());
        assertThat(response.getAddresserId()).isEqualTo(friend.getAddresser().getId());
        assertThat(response.getAccepted()).isEqualTo(false);
        assertThat(response.getRejected()).isEqualTo(false);
    }
    
    @Test 
    void shouldMapFriendDTOtoFriendEntity() {
        Friend request = utils.createFriendRequest(null, null, false, false);
        FriendDTO dto = utils.createFriendRequestDTO(request);

        Friend requestMapped = friendMapper.toEntity(dto, request.getRequester(), request.getAddresser());

        assertThat(requestMapped).isNotNull();
        assertThat(requestMapped.getId()).isEqualTo(dto.getId());
        assertThat(requestMapped.getRequester().getId()).isEqualTo(request.getRequester().getId());
        assertThat(requestMapped.getAddresser().getId()).isEqualTo(request.getAddresser().getId());
        assertThat(requestMapped.getAccepted()).isEqualTo(dto.getAccepted());
        assertThat(requestMapped.getRejected()).isEqualTo(dto.getRejected());
    }

    @Test 
    void shouldPartiallyMapFriendDTOtoFriendEntity() {
            Friend friend = utils.createFriendRequest(null, null, false, false);

            // Not changing ID
            FriendDTO dto = FriendDTO.builder()
                            .id(friend.getId() + 1)
                            .build();
            Friend updateFriend = friendMapper.updateModel(dto, friend);
            assertThat(updateFriend).isNotNull();
            assertThat(updateFriend.getId()).isEqualTo(friend.getId());

            // Not changing Requester ID
            dto = FriendDTO.builder()
                            .requesterId(friend.getRequester().getId() + 1)
                            .build();
            updateFriend = friendMapper.updateModel(dto, friend);
            assertThat(updateFriend).isNotNull();
            assertThat(updateFriend.getRequester().getId()).isEqualTo(friend.getRequester().getId());

            // Not changing Addreser ID
            dto = FriendDTO.builder()
                            .addresserId(friend.getAddresser().getId() + 1)
                            .build();
            updateFriend = friendMapper.updateModel(dto, friend);
            assertThat(updateFriend).isNotNull();
            assertThat(updateFriend.getAddresser().getId()).isEqualTo(friend.getAddresser().getId());

            // Changing accepted
            dto = FriendDTO.builder()
                            .accepted(true)
                            .build();
            updateFriend = friendMapper.updateModel(dto, friend);
            assertThat(updateFriend).isNotNull();
            assertThat(updateFriend.getAccepted()).isEqualTo(dto.getAccepted());

            // Changing rejected
            dto = FriendDTO.builder()
                            .rejected(true)
                            .build();
            updateFriend = friendMapper.updateModel(dto, friend);
            assertThat(updateFriend).isNotNull();
            assertThat(updateFriend.getRejected()).isEqualTo(dto.getRejected());
    }

    @Test 
    void shouldMapReviewEntityToReviewDTO() {
        Review review = utils.createReview(null, null);

        ReviewDTO response = reviewMapper.toDTO(review);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(review.getId());
        assertThat(response.getClientId()).isEqualTo(review.getClient().getId());
        assertThat(response.getBranchId()).isEqualTo(review.getBranch().getId());
        assertThat(response.getText()).isEqualTo(review.getText());
        assertThat(response.getDate()).isEqualTo(review.getDate());
    }
    
    @Test 
    void shouldMapReviewDTOtoReviewEntity() {
        Review request = utils.createReview(null, null);
        ReviewDTO dto = utils.createReviewDTO(request);

        Review requestMapped = reviewMapper.toEntity(dto, request.getClient(), request.getBranch());

        assertThat(requestMapped).isNotNull();
        assertThat(requestMapped.getId()).isEqualTo(dto.getId());
        assertThat(requestMapped.getClient().getId()).isEqualTo(request.getClient().getId());
        assertThat(requestMapped.getBranch().getId()).isEqualTo(request.getBranch().getId());
        assertThat(requestMapped.getText()).isEqualTo(dto.getText());
        assertThat(requestMapped.getDate()).isEqualTo(dto.getDate());
    }

    @Test 
    void shouldPartiallyMapReviewDTOtoReviewEntity() {
            Review review = utils.createReview(null, null);

            // Not changing ID
            ReviewDTO dto = ReviewDTO.builder()
                            .id(review.getId() + 1)
                            .build();
            Review updateReview = reviewMapper.updateModel(dto, review);
            assertThat(updateReview).isNotNull();
            assertThat(updateReview.getId()).isEqualTo(review.getId());

            // Not changing Client ID
            dto = ReviewDTO.builder()
                            .clientId(review.getClient().getId() + 1)
                            .build();
            updateReview = reviewMapper.updateModel(dto, review);
            assertThat(updateReview).isNotNull();
            assertThat(updateReview.getClient().getId()).isEqualTo(review.getClient().getId());

            // Not changing Branch ID
            dto = ReviewDTO.builder()
                            .branchId(review.getBranch().getId() + 1)
                            .build();
            updateReview = reviewMapper.updateModel(dto, review);
            assertThat(updateReview).isNotNull();
            assertThat(updateReview.getBranch().getId()).isEqualTo(review.getBranch().getId());

            // Changing text
            dto = ReviewDTO.builder()
                            .text(review.getText() + "_test")
                            .build();
            updateReview = reviewMapper.updateModel(dto, review);
            assertThat(updateReview).isNotNull();
            assertThat(updateReview.getText()).isEqualTo(dto.getText());

            // Changing date
            dto = ReviewDTO.builder()
                            .date(new Date(System.currentTimeMillis()))
                            .build();
            updateReview = reviewMapper.updateModel(dto, review);
            assertThat(updateReview).isNotNull();
            assertThat(updateReview.getDate()).isEqualTo(dto.getDate());
    }
}