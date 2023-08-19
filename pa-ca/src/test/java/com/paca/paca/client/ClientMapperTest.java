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
                client.getIdentityDocument(),
                client.getAddress(),
                client.getPhoneNumber(),
                client.getStripeCustomerId(),
                client.getDateOfBirth());

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
        ClientDTO dto = new ClientDTO(
                client.getId() + 1,
                client.getUser().getId() + 1,
                client.getUser().getEmail() + ".",
                client.getName() + ".",
                client.getSurname() + ".",
                client.getIdentityDocument() + ".",
                client.getAddress() + ".",
                client.getPhoneNumber() + ".",
                client.getStripeCustomerId() + ".",
                new Date(System.currentTimeMillis()));
        Client response = clientMapper.updateModel(dto, client);
        Client expected = new Client(
                client.getId(),
                client.getUser(),
                dto.getName(),
                dto.getSurname(),
                dto.getIdentityDocument(),
                dto.getAddress(),
                dto.getPhoneNumber(),
                dto.getStripeCustomerId(),
                dto.getDateOfBirth());

        assertThat(response).isEqualTo(expected);
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

        FriendDTO dto = new FriendDTO(
                friend.getId() + 1,
                friend.getRequester().getId() + 1,
                friend.getAddresser().getId() + 1,
                !friend.getAccepted(),
                !friend.getRejected());
        Friend response = friendMapper.updateModel(dto, friend);
        Friend expected = new Friend(
                friend.getId(),
                friend.getRequester(),
                friend.getAddresser(),
                dto.getAccepted(),
                dto.getRejected());

        assertThat(response).isEqualTo(expected);
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

        ReviewDTO dto = new ReviewDTO(
                review.getId() + 1,
                review.getClient().getId() + 1,
                review.getBranch().getId() + 1,
                review.getText() + ".",
                new Date(System.currentTimeMillis()),
                null);
        Review response = reviewMapper.updateModel(dto, review);
        Review expected = new Review(
                review.getId(),
                review.getClient(),
                review.getBranch(),
                dto.getText(),
                dto.getDate());

        assertThat(response).isEqualTo(expected);
    }

}