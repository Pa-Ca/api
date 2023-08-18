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
import com.paca.paca.client.model.Review;
import com.paca.paca.branch.model.Branch;
import com.paca.paca.client.model.ReviewLike;
import com.paca.paca.client.model.ClientGuest;
import com.paca.paca.client.model.FavoriteBranch;
import com.paca.paca.user.repository.RoleRepository;
import com.paca.paca.user.repository.UserRepository;
import com.paca.paca.branch.repository.BranchRepository;
import com.paca.paca.client.repository.ClientRepository;
import com.paca.paca.client.repository.FriendRepository;
import com.paca.paca.client.repository.ReviewRepository;
import com.paca.paca.reservation.model.Guest;
import com.paca.paca.business.repository.BusinessRepository;
import com.paca.paca.client.repository.ReviewLikeRepository;
import com.paca.paca.client.repository.ClientGuestRepository;
import com.paca.paca.client.repository.FavoriteBranchRepository;

import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.test.context.ActiveProfiles;
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
public class ClientRepositoryTest extends PacaTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private FriendRepository friendRepository;

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private ReviewLikeRepository reviewLikeRepository;

    @Autowired
    private ClientGuestRepository clientGuestRepository;

    @Autowired
    private FavoriteBranchRepository favoriteBranchRepository;

    private TestUtils utils;

    @BeforeAll
    void initUtils() {
        utils = TestUtils.builder()
                .roleRepository(roleRepository)
                .userRepository(userRepository)
                .clientRepository(clientRepository)
                .reviewRepository(reviewRepository)
                .friendRepository(friendRepository)
                .branchRepository(branchRepository)
                .businessRepository(businessRepository)
                .reviewLikeRepository(reviewLikeRepository)
                .favoriteBranchRepository(favoriteBranchRepository)
                .build();
    }

    @BeforeEach
    void restoreClientDB() {
        clientRepository.deleteAll();
    }

    @AfterEach
    void restoreTest() {
        favoriteBranchRepository.deleteAll();
        reviewLikeRepository.deleteAll();
        reviewRepository.deleteAll();
        branchRepository.deleteAll();
        clientRepository.deleteAll();
        businessRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    void shouldCheckThatClientExistsByUserEmail() {
        User user = utils.createUser();
        Client client = utils.createClient(user);

        boolean expected = clientRepository.existsByUserEmail(user.getEmail());
        Optional<Client> expectedClient = clientRepository.findByUserEmail(user.getEmail());

        assertThat(expected).isTrue();
        assertThat(expectedClient).isEqualTo(client);
    }

    @Test
    void shouldCheckThatClientDoesNotExistsByUserEmail() {
        User user = utils.createUser();
        utils.createClient(user);

        boolean expected = clientRepository.existsByUserEmail(user.getEmail() + "a");
        Optional<Client> expectedClient = clientRepository.findByUserEmail(user.getEmail() + "a");

        assertThat(expected).isFalse();
        assertThat(expectedClient.isEmpty()).isTrue();
    }

    @Test
    void shouldCheckThatFriendRequestExistsByRequesterIdAndAddresserId() {
        Friend request = utils.createFriendRequest(null, null, false, false);

        boolean expected = friendRepository.existsByRequesterIdAndAddresserId(
                request.getRequester().getId(),
                request.getAddresser().getId());
        Optional<Friend> expectedRequest = friendRepository.findByRequesterIdAndAddresserId(
                request.getRequester().getId(),
                request.getAddresser().getId());

        assertThat(expected).isTrue();
        assertThat(expectedRequest).isEqualTo(request);
    }

    @Test
    void shouldCheckThatFriendRequestDoesNotExistsByRequesterIdAndAddresserId() {
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
    void shouldGetAllFriendRequestsByRequesterIdAndAcceptedTrue() {
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
    void shouldGetAllFriendRequestsByAddresserIdAndAcceptedTrue() {
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
    void shouldGetAllFriendRequestsByAddresserIdAndRejectedTrue() {
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
    void shouldGetAllFriendRequestsByAddresserIdAndAcceptedFalseAndRejectedFalse() {
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

    @Test
    void shouldGetFavoriteBranchExistsByClientIdAndBranchId() {
        Client client = utils.createClient(null);
        Branch branch = utils.createBranch(null);
        FavoriteBranch favoriteBranch = utils.createFavoriteBranch(client, branch);

        boolean expected = favoriteBranchRepository.existsByClientIdAndBranchId(client.getId(), branch.getId());
        Optional<FavoriteBranch> expectedFavoriteBranch = favoriteBranchRepository.findByClientIdAndBranchId(
                client.getId(),
                branch.getId());

        assertThat(expected).isTrue();
        assertThat(expectedFavoriteBranch.get()).isEqualTo(favoriteBranch);
    }

    @Test
    void shouldCheckThatFavoriteBranchDoesNotExistsByClientIdAndBranchId() {
        Client client = utils.createClient(null);
        Branch branch = utils.createBranch(null);

        boolean expected = favoriteBranchRepository.existsByClientIdAndBranchId(client.getId(), branch.getId());

        assertThat(expected).isFalse();
    }

    @Test
    void shouldGetAllFavoriteBranchByClientId() {
        Client client = utils.createClient(null);
        FavoriteBranch fav1 = utils.createFavoriteBranch(client, null);
        FavoriteBranch fav2 = utils.createFavoriteBranch(client, null);
        utils.createFavoriteBranch(null, null);
        utils.createFavoriteBranch(null, null);

        List<FavoriteBranch> favoriteBranches = favoriteBranchRepository.findAllByClientId(client.getId());

        assertThat(favoriteBranches.size()).isEqualTo(2);
        assertThat(favoriteBranches.contains(fav1)).isTrue();
        assertThat(favoriteBranches.contains(fav2)).isTrue();
    }

    @Test
    void shouldCheckThatReviewExistsById() {
        Review review = utils.createReview(null, null);

        boolean expected = reviewRepository.existsById(review.getId());
        Optional<Review> expectedReview = reviewRepository.findById(review.getId());

        assertThat(expected).isTrue();
        assertThat(expectedReview.get()).isEqualTo(review);
    }

    @Test
    void shouldCheckThatReviewDoesNotExistsById() {
        boolean expected = reviewRepository.existsById(1L);
        Optional<Review> expectedReview = reviewRepository.findById(1L);

        assertThat(expected).isFalse();
        assertThat(expectedReview.isEmpty()).isTrue();
    }

    @Test
    void shouldCheckThatReviewExistsByIdAndClientId() {
        Client client = utils.createClient(null);
        Review review = utils.createReview(client, null);

        boolean expected = reviewRepository.existsByIdAndClientId(review.getId(), client.getId());

        assertThat(expected).isTrue();
    }

    @Test
    void shouldCheckThatReviewDoesNotExistsByIdAndClientId() {
        Review review = utils.createReview(null, null);

        boolean expected = reviewRepository.existsByIdAndClientId(review.getId(), 1L);

        assertThat(expected).isFalse();
    }

    @Test
    void shouldGetReviewByClientIdAndBranchId() {
        Client client = utils.createClient(null);
        Branch branch = utils.createBranch(null);
        Review review = utils.createReview(client, branch);

        Optional<Review> expectedReview = reviewRepository.findByClientIdAndBranchId(
                client.getId(),
                branch.getId());

        assertThat(expectedReview.get()).isEqualTo(review);
    }

    @Test
    void shouldCheckThatReviewDoesNotExistsByClientIdAndBranchId() {
        Client client = utils.createClient(null);
        Branch branch = utils.createBranch(null);

        Optional<Review> expectedReview = reviewRepository.findByClientIdAndBranchId(client.getId(), branch.getId());

        assertThat(expectedReview).isEmpty();
    }

    @Test
    void shouldGetAllReviewByBranchId() {
        Branch branch = utils.createBranch(null);
        Review review1 = utils.createReview(null, branch);
        Review review2 = utils.createReview(null, branch);
        utils.createReview(null, null);
        utils.createReview(null, null);

        List<Review> reviews = reviewRepository.findAllByBranchId(branch.getId());

        assertThat(reviews.size()).isEqualTo(2);
        assertThat(reviews.contains(review1)).isTrue();
        assertThat(reviews.contains(review2)).isTrue();
    }

    @Test
    void shouldCheckThatReviewLikeExistsByClientIdAndReviewId() {
        Client client = utils.createClient(null);
        Review review = utils.createReview(null, null);
        ReviewLike like = utils.createReviewLike(client, review);

        boolean expected = reviewLikeRepository.existsByClientIdAndReviewId(client.getId(), review.getId());
        Optional<ReviewLike> expectedLike = reviewLikeRepository.findByClientIdAndReviewId(client.getId(),
                review.getId());

        assertThat(expected).isTrue();
        assertThat(expectedLike.get()).isEqualTo(like);
    }

    @Test
    void shouldCheckThatReviewLikeDoesNotExistsByClientIdAndReviewId() {
        boolean expected = reviewLikeRepository.existsByClientIdAndReviewId(1L, 1L);
        Optional<ReviewLike> expectedLike = reviewLikeRepository.findByClientIdAndReviewId(1L, 1L);

        assertThat(expected).isFalse();
        assertThat(expectedLike.isEmpty()).isTrue();
    }

    @Test
    void shouldGetAllReviewLikeByReviewId() {
        Review review = utils.createReview(null, null);
        ReviewLike like1 = utils.createReviewLike(null, review);
        ReviewLike like2 = utils.createReviewLike(null, review);
        utils.createReviewLike(null, null);
        utils.createReviewLike(null, null);

        List<ReviewLike> likes = reviewLikeRepository.findAllByReviewId(review.getId());

        assertThat(likes.size()).isEqualTo(2);
        assertThat(likes.contains(like1)).isTrue();
        assertThat(likes.contains(like2)).isTrue();
    }

    @Test
    void shouldGetClientGuestByClientId() {
        Client client = utils.createClient(null);
        ClientGuest clientGuest = utils.createClientGuest(client);

        Optional<ClientGuest> expectedGuest = clientGuestRepository.findByClientId(clientGuest.getId());

        assertThat(expectedGuest.get()).isEqualTo(clientGuest);
    }

    @Test
    void shouldDoesNotGetClientGuestByClientId() {
        Client client = utils.createClient(null);
        ClientGuest clientGuest = utils.createClientGuest(client);

        Optional<ClientGuest> expectedGuest = clientGuestRepository.findByClientId(clientGuest.getId() + 1);

        assertThat(expectedGuest.isEmpty()).isTrue();
    }

    @Test
    void shouldGetClientGuestByGuestId() {
        Guest guest = utils.createGuest();
        ClientGuest clientGuest = utils.createClientGuest(guest);

        Optional<ClientGuest> expectedGuest = clientGuestRepository.findByGuestId(clientGuest.getId());

        assertThat(expectedGuest.get()).isEqualTo(guest);
    }

    @Test
    void shouldDoesNotGetClientGuestByGuestId() {
        Guest guest = utils.createGuest();
        ClientGuest clientGuest = utils.createClientGuest(guest);

        Optional<ClientGuest> expectedGuest = clientGuestRepository.findByGuestId(clientGuest.getId() + 1);

        assertThat(expectedGuest.isEmpty()).isTrue();
    }

}
