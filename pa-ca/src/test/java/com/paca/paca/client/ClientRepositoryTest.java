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
import com.paca.paca.client.model.FavoriteBranch;
import com.paca.paca.user.repository.RoleRepository;
import com.paca.paca.user.repository.UserRepository;
import com.paca.paca.branch.repository.BranchRepository;
import com.paca.paca.client.repository.ClientRepository;
import com.paca.paca.client.repository.FriendRepository;
import com.paca.paca.client.repository.ReviewRepository;
import com.paca.paca.business.repository.BusinessRepository;
import com.paca.paca.client.repository.ReviewLikeRepository;
import com.paca.paca.client.repository.FavoriteBranchRepository;

import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.util.List;
import java.util.Date;
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
    void shouldCreateClient() {
        User user = utils.createUser();
        Client client = Client.builder()
                .id(1L)
                .user(user)
                .name("test")
                .surname("Test")
                .stripeCustomerId("stripe_id_test")
                .phoneNumber("+580000000")
                .address("Test address")
                .dateOfBirth(new Date(System.currentTimeMillis()))
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
    void shouldGetAllClients() {
        int nUsers = 10;

        for (int i = 0; i < nUsers; i++) {
            utils.createClient(null);
        }

        List<Client> clients = clientRepository.findAll();

        assertThat(clients.size()).isEqualTo(nUsers);
    }

    @Test
    void shouldCheckThatClientExistsById() {
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
    void shouldCheckThatClientExistsByUserId() {
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
    void shouldCheckThatClientDoesNotExistsByUserId() {
        User user = utils.createUser();
        utils.createClient(user);

        boolean expected = clientRepository.existsByUserId(user.getId() + 1);
        Optional<Client> expectedClient = clientRepository.findByUserId(user.getId() + 1);

        assertThat(expected).isFalse();
        assertThat(expectedClient.isEmpty()).isTrue();
    }

    @Test
    void shouldCheckThatClientExistsByUserEmail() {
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
    void shouldCheckThatClientDoesNotExistsByUserEmail() {
        User user = utils.createUser();
        utils.createClient(user);

        boolean expected = clientRepository.existsByUserEmail(user.getEmail() + "a");
        Optional<Client> expectedClient = clientRepository.findByUserEmail(user.getEmail() + "a");

        assertThat(expected).isFalse();
        assertThat(expectedClient.isEmpty()).isTrue();
    }

    @Test
    void shouldDeleteClient() {
        Client client = utils.createClient(null);
        
        clientRepository.delete(client);

        List<Client> clients = clientRepository.findAll();
        assertThat(clients.size()).isEqualTo(0);
    }

    @Test 
    void shouldCheckThatFriendRequestExistsById() {
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
    void shouldCheckThatFriendRequestDoesNotExistsById() {
        Friend request = utils.createFriendRequest(null, null, false, false);
        
        boolean expected = friendRepository.existsById(request.getId() + 1);
        Optional<Friend> expectedRequest = friendRepository.findById(request.getId() + 1);

        assertThat(expected).isFalse();
        assertThat(expectedRequest.isEmpty()).isTrue();
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
        assertThat(expectedRequest.isPresent()).isTrue();
        assertThat(expectedRequest.get().getId()).isEqualTo(request.getId());
        assertThat(expectedRequest.get().getRequester().getId()).isEqualTo(request.getRequester().getId());
        assertThat(expectedRequest.get().getAddresser().getId()).isEqualTo(request.getAddresser().getId());
        assertThat(expectedRequest.get().getAccepted()).isEqualTo(request.getAccepted());
        assertThat(expectedRequest.get().getRejected()).isEqualTo(request.getRejected());
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
    void shouldGetAllFriendRequestsByRequesterIdAndRejectedTrue() {
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
    void shouldGetAllFriendRequestsByRequesterIdAndAcceptedFalseAndRejectedFalse() {
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
    void shouldCreateFavoriteBranch() {
        Client client = utils.createClient(null);
        Branch branch = utils.createBranch(null);
        FavoriteBranch fav = FavoriteBranch.builder()
                .client(client)
                .branch(branch)
                .build();

        FavoriteBranch savedFav = favoriteBranchRepository.save(fav);

        assertThat(savedFav).isNotNull();
        assertThat(savedFav.getClient().getId()).isEqualTo(fav.getClient().getId());
        assertThat(savedFav.getBranch().getId()).isEqualTo(fav.getBranch().getId());
    }

    @Test
    void shouldGetFavoriteBranchExistsByClientIdAndBranchId() {
        Client client = utils.createClient(null);
        Branch branch = utils.createBranch(null);
        FavoriteBranch favoriteBranch = utils.createFavoriteBranch(client, branch);

        boolean expected = favoriteBranchRepository.existsByClientIdAndBranchId(client.getId(), branch.getId());
        Optional<FavoriteBranch> expectedFavoriteBranch = favoriteBranchRepository.findByClientIdAndBranchId(
            client.getId(), 
            branch.getId()
        );

        assertThat(expected).isTrue();
        assertThat(expectedFavoriteBranch).isNotEmpty();
        assertThat(expectedFavoriteBranch.get().getClient().getId()).isEqualTo(favoriteBranch.getClient().getId());
        assertThat(expectedFavoriteBranch.get().getBranch().getId()).isEqualTo(favoriteBranch.getBranch().getId());
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
    void shouldGetAllFavoriteBranchByBranchId() {
        Branch branch = utils.createBranch(null);
        FavoriteBranch fav1 = utils.createFavoriteBranch(null, branch);
        FavoriteBranch fav2 = utils.createFavoriteBranch(null, branch);
        utils.createFavoriteBranch(null, null);
        utils.createFavoriteBranch(null, null);

        List<FavoriteBranch> favoriteBranches = favoriteBranchRepository.findAllByBranchId(branch.getId());

        assertThat(favoriteBranches.size()).isEqualTo(2);
        assertThat(favoriteBranches.contains(fav1)).isTrue();
        assertThat(favoriteBranches.contains(fav2)).isTrue();
    }

    @Test
    void shouldDeleteFavoriteBranch() {
        FavoriteBranch fav = utils.createFavoriteBranch(null, null);

        favoriteBranchRepository.delete(fav);

        List<FavoriteBranch> favs = favoriteBranchRepository.findAll();
        assertThat(favs.size()).isEqualTo(0);
    }

    @Test
    void shouldCreateReview() {
        Client client = utils.createClient(null);
        Branch branch = utils.createBranch(null);
        Review review = Review.builder()
                .client(client)
                .branch(branch)
                .text("text")
                .date(new Date(System.currentTimeMillis()))
                .build();

        Review savedReview = reviewRepository.save(review);

        assertThat(savedReview).isNotNull();
        assertThat(savedReview.getClient().getId()).isEqualTo(review.getClient().getId());
        assertThat(savedReview.getBranch().getId()).isEqualTo(review.getBranch().getId());
        assertThat(savedReview.getText()).isEqualTo(review.getText());
        assertThat(savedReview.getDate()).isEqualTo(review.getDate());
    }

    @Test
    void shouldCheckThatReviewExistsById() {
        Review review = utils.createReview(null, null);

        boolean expected = reviewRepository.existsById(review.getId());
        Optional<Review> expectedReview = reviewRepository.findById(review.getId());

        assertThat(expected).isTrue();
        assertThat(expectedReview.isPresent()).isTrue();
        assertThat(expectedReview.get().getId()).isEqualTo(review.getId());
        assertThat(expectedReview.get().getClient().getId()).isEqualTo(review.getClient().getId());
        assertThat(expectedReview.get().getBranch().getId()).isEqualTo(review.getBranch().getId());
        assertThat(expectedReview.get().getText()).isEqualTo(review.getText());
        assertThat(expectedReview.get().getDate()).isEqualTo(review.getDate());
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
            branch.getId()
        );

        assertThat(expectedReview).isNotEmpty();
        assertThat(expectedReview.get().getClient().getId()).isEqualTo(review.getClient().getId());
        assertThat(expectedReview.get().getBranch().getId()).isEqualTo(review.getBranch().getId());
        assertThat(expectedReview.get().getText()).isEqualTo(review.getText());
        assertThat(expectedReview.get().getDate()).isEqualTo(review.getDate());
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
    void shouldDeleteReview() {
        Review review = utils.createReview(null, null);

        reviewRepository.delete(review);

        List<Review> reviews = reviewRepository.findAll();
        assertThat(reviews.size()).isEqualTo(0);
    }

    @Test
    void shouldCreateReviewLike() {
        Client client = utils.createClient(null);
        Review review = utils.createReview(null, null);
        ReviewLike like = ReviewLike.builder()
                .client(client)
                .review(review)
                .build();

        ReviewLike savedLike = reviewLikeRepository.save(like);

        assertThat(savedLike).isNotNull();
        assertThat(savedLike.getClient().getId()).isEqualTo(like.getClient().getId());
        assertThat(savedLike.getReview().getId()).isEqualTo(like.getReview().getId());
    }

    @Test
    void shouldCheckThatReviewLikeExistsById() {
        ReviewLike like = utils.createReviewLike(null, null);

        boolean expected = reviewLikeRepository.existsById(like.getId());
        Optional<ReviewLike> expectedLike = reviewLikeRepository.findById(like.getId());

        assertThat(expected).isTrue();
        assertThat(expectedLike.isPresent()).isTrue();
        assertThat(expectedLike.get().getId()).isEqualTo(like.getId());
        assertThat(expectedLike.get().getClient().getId()).isEqualTo(like.getClient().getId());
        assertThat(expectedLike.get().getReview().getId()).isEqualTo(like.getReview().getId());
    }

    @Test
    void shouldCheckThatReviewLikeDoesNotExistsById() {
        boolean expected = reviewLikeRepository.existsById(1L);
        Optional<ReviewLike> expectedLike = reviewLikeRepository.findById(1L);

        assertThat(expected).isFalse();
        assertThat(expectedLike.isEmpty()).isTrue();
    }

    @Test
    void shouldCheckThatReviewLikeExistsByClientIdAndReviewId() {
        Client client = utils.createClient(null);
        Review review = utils.createReview(null, null);
        ReviewLike like = utils.createReviewLike(client, review);

        boolean expected = reviewLikeRepository.existsByClientIdAndReviewId(client.getId(), review.getId());
        Optional<ReviewLike> expectedLike = reviewLikeRepository.findByClientIdAndReviewId(client.getId(), review.getId());

        assertThat(expected).isTrue();
        assertThat(expectedLike.isPresent()).isTrue();
        assertThat(expectedLike.get().getId()).isEqualTo(like.getId());
        assertThat(expectedLike.get().getClient().getId()).isEqualTo(like.getClient().getId());
        assertThat(expectedLike.get().getReview().getId()).isEqualTo(like.getReview().getId());
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
    void shouldDeleteReviewLike() {
        ReviewLike like = utils.createReviewLike(null, null);

        reviewLikeRepository.delete(like);

        List<ReviewLike> likes = reviewLikeRepository.findAll();
        assertThat(likes.size()).isEqualTo(0);
    }
}
