package com.paca.paca.client;

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

import org.mockito.InjectMocks;
import org.junit.jupiter.api.Test;
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
        ClientDTO expected = new ClientDTO(
                client.getId(),
                client.getUser().getId(),
                client.getUser().getEmail(),
                client.getName(),
                client.getSurname(),
                client.getStripeCustomerId(),
                client.getPhoneNumber(),
                client.getAddress(),
                client.getDateOfBirth(),
                client.getIdentityDocument());

        assertThat(response).isEqualTo(expected);
    }

    @Test
    void shouldMapClientDTOtoClientEntity() {
        User user = utils.createUser();
        ClientDTO dto = utils.createClientDTO(utils.createClient(user));

        Client client = clientMapper.toEntity(dto, user);
        Client expected = new Client(
                dto.getId(),
                user,
                dto.getName(),
                dto.getSurname(),
                dto.getIdentityDocument(),
                dto.getAddress(),
                dto.getPhoneNumber(),
                dto.getStripeCustomerId(),
                dto.getDateOfBirth());

        assertThat(client).isEqualTo(expected);
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

        // Not change identity document
        dto = ClientDTO.builder()
                .identityDocument("V0")
                .build();
        updatedClient = clientMapper.updateModel(dto, client);
        assertThat(updatedClient).isNotNull();
        assertThat(updatedClient.getIdentityDocument()).isEqualTo(client.getIdentityDocument());
    }

    @Test
    void shouldMapFriendEntityToFriendDTO() {
        Friend friend = utils.createFriendRequest(null, null, false, false);

        FriendDTO response = friendMapper.toDTO(friend);
        FriendDTO expected = new FriendDTO(
                friend.getId(),
                friend.getRequester().getId(),
                friend.getAddresser().getId(),
                friend.getAccepted(),
                friend.getRejected());

        assertThat(response).isEqualTo(expected);
    }

    @Test
    void shouldMapFriendDTOtoFriendEntity() {
        Friend request = utils.createFriendRequest(null, null, false, false);
        FriendDTO dto = utils.createFriendRequestDTO(request);

        Friend requestMapped = friendMapper.toEntity(dto, request.getRequester(), request.getAddresser());
        Friend expected = new Friend(
                dto.getId(),
                request.getRequester(),
                request.getAddresser(),
                dto.getAccepted(),
                dto.getRejected());

        assertThat(requestMapped).isEqualTo(expected);
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
        ReviewDTO expected = new ReviewDTO(
                review.getId(),
                review.getClient().getId(),
                review.getBranch().getId(),
                review.getText(),
                review.getDate(),
                null);

        assertThat(response).isEqualTo(expected);
    }

    @Test
    void shouldMapReviewDTOtoReviewEntity() {
        Review request = utils.createReview(null, null);
        ReviewDTO dto = utils.createReviewDTO(request);

        Review requestMapped = reviewMapper.toEntity(dto, request.getClient(), request.getBranch());
        Review expected = new Review(
                dto.getId(),
                request.getClient(),
                request.getBranch(),
                dto.getText(),
                dto.getDate());

        assertThat(requestMapped).isEqualTo(expected);
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