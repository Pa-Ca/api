package com.paca.paca.utils;

import java.util.List;
import java.util.Date;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import com.paca.paca.user.model.Role;
import com.paca.paca.user.model.User;
import com.paca.paca.user.dto.UserDTO;
import com.paca.paca.statics.UserRole;
import com.paca.paca.business.tier.Tier;
import com.paca.paca.client.model.Client;
import com.paca.paca.client.model.Friend;
import com.paca.paca.client.model.Review;
import com.paca.paca.branch.model.Branch;
import com.paca.paca.statics.BusinessTier;
import com.paca.paca.branch.dto.BranchDTO;
import com.paca.paca.branch.model.Amenity;
import com.paca.paca.client.dto.ClientDTO;
import com.paca.paca.client.dto.FriendDTO;
import com.paca.paca.client.dto.ReviewDTO;
import com.paca.paca.branch.dto.AmenityDTO;
import com.paca.paca.client.model.ReviewLike;
import com.paca.paca.business.model.Business;
import com.paca.paca.branch.model.BranchAmenity;
import com.paca.paca.client.model.FavoriteBranch;
import com.paca.paca.reservation.model.Reservation;
import com.paca.paca.reservation.dto.ReservationDTO;
import com.paca.paca.user.repository.RoleRepository;
import com.paca.paca.user.repository.UserRepository;
import com.paca.paca.client.repository.ClientRepository;
import com.paca.paca.client.repository.FriendRepository;
import com.paca.paca.client.repository.ReviewRepository;
import com.paca.paca.branch.repository.BranchRepository;
import com.paca.paca.branch.repository.AmenityRepository;
import com.paca.paca.client.repository.ReviewLikeRepository;
import com.paca.paca.business.repository.BusinessRepository;
import com.paca.paca.branch.repository.BranchAmenityRepository;
import com.paca.paca.client.repository.FavoriteBranchRepository;
import com.paca.paca.product_sub_category.model.ProductCategory;
import com.paca.paca.reservation.repository.ReservationRepository;
import com.paca.paca.product_sub_category.model.ProductSubCategory;
import com.paca.paca.product_sub_category.repository.ProductCategoryRepository;
import com.paca.paca.product_sub_category.repository.ProductSubCategoryRepository;

import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;

@Setter
@Getter
@Builder
public class TestUtils {

    RoleRepository roleRepository;

    UserRepository userRepository;

    ClientRepository clientRepository;

    FriendRepository friendRepository;

    BranchRepository branchRepository;

    ReviewRepository reviewRepository;

    AmenityRepository amenityRepository;

    BusinessRepository businessRepository;

    ReviewLikeRepository reviewLikeRepository;

    ReservationRepository reservationRepository;

    BranchAmenityRepository branchAmenityRepository;

    FavoriteBranchRepository favoriteBranchRepository;

    ProductCategoryRepository productCategoryRepository;

    ProductSubCategoryRepository productSubCategoryRepository;

    public static <T> List<T> castList(Class<? extends T> clazz, List<?> rawCollection) {
        List<T> result = new ArrayList<>(rawCollection.size());

        for (int i = 0; i < rawCollection.size(); i++) {
            result.add(clazz.cast(rawCollection.get(i)));
        }

        return result;
    }

    public void setAuthorities(String role) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role));
        Authentication auth = new UsernamePasswordAuthenticationToken(
                "user",
                "password",
                authorities);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);
    }

    public User createUser() {
        Role role = Role.builder()
                .id((long) UserRole.client.ordinal())
                .name(UserRole.client)
                .build();
        User user = User.builder()
                .id(ThreadLocalRandom.current().nextLong(999999999))
                .email("test@test.com")
                .password("123456789aA#")
                .verified(false)
                .loggedIn(false)
                .role(role)
                .build();

        if (roleRepository != null) {
            roleRepository.save(user.getRole());
        }
        if (userRepository != null) {
            user = userRepository.save(user);
        }

        return user;
    }

    public UserDTO createUserDTO(User user) {
        if (user == null) {
            user = createUser();
        }
        UserDTO dto = UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .verified(user.getVerified())
                .loggedIn(user.getLoggedIn())
                .role(user.getRole().getName().name())
                .build();

        return dto;
    }

    public Client createClient(User user) {
        if (user == null) {
            user = createUser();
        }
        Client client = Client.builder()
                .id(ThreadLocalRandom.current().nextLong(999999999))
                .user(user)
                .name("test")
                .surname("Test")
                .stripeCustomerId("stripe_id_test")
                .phoneNumber("+580000000")
                .address("Test address")
                .dateOfBirth(new Date(System.currentTimeMillis()))
                .build();
        if (clientRepository != null) {
            client = clientRepository.save(client);
        }
        return client;
    }

    public ClientDTO createClientDTO(Client client) {
        if (client == null) {
            client = createClient(null);
        }
        ClientDTO dto = ClientDTO.builder()
                .id(client.getId())
                .userId(client.getUser().getId())
                .email(client.getUser().getEmail())
                .name(client.getName())
                .surname(client.getSurname())
                .stripeCustomerId(client.getStripeCustomerId())
                .phoneNumber(client.getPhoneNumber())
                .address(client.getAddress())
                .dateOfBirth(client.getDateOfBirth())
                .build();

        return dto;
    }

    public Friend createFriendRequest(
            Client requester,
            Client addresser,
            boolean accepted,
            boolean rejected) {
        if (requester == null) {
            requester = createClient(null);
        }
        if (addresser == null) {
            addresser = createClient(null);
        }

        Friend request = Friend.builder()
                .id(ThreadLocalRandom.current().nextLong(999999999))
                .requester(requester)
                .addresser(addresser)
                .accepted(accepted)
                .rejected(rejected)
                .build();

        if (friendRepository != null) {
            request = friendRepository.save(request);
        }

        return request;
    }

    public FriendDTO createFriendRequestDTO(Friend request) {
        if (request == null) {
            request = createFriendRequest(null, null, false, false);
        }
        FriendDTO dto = FriendDTO.builder()
                .id(request.getId())
                .requesterId(request.getRequester().getId())
                .addresserId(request.getAddresser().getId())
                .accepted(request.getAccepted())
                .rejected(request.getRejected())
                .build();

        return dto;
    }

    public Business createBusiness(User user) {
        if (user == null) {
            user = createUser();
        }
        Tier tier = Tier.builder()
                .id((long) BusinessTier.basic.ordinal())
                .name(BusinessTier.basic)
                .build();
        Business business = Business.builder()
                .id(ThreadLocalRandom.current().nextLong(999999999))
                .user(user)
                .name("Test")
                .verified(false)
                .tier(tier)
                .build();

        if (businessRepository != null) {
            business = businessRepository.save(business);
        }
        return business;
    }

    public Branch createBranch(Business business) {
        if (business == null) {
            business = createBusiness(null);
        }

        Branch branch = Branch.builder()
                .id(ThreadLocalRandom.current().nextLong(999999999))
                .business(business)
                .address("address test")
                .coordinates("coordinates test")
                .name("name test")
                .overview("overview test")
                .score(4.0F)
                .capacity(42)
                .reservationPrice(37.0F)
                .reserveOff(false)
                .build();

        if (branchRepository != null) {
            branch = branchRepository.save(branch);
        }

        return branch;
    }

    public BranchDTO createBranchDTO(Branch branch) {
        if (branch == null) {
            branch = createBranch(null);
        }
        BranchDTO dto = BranchDTO.builder()
                .id(branch.getId())
                .businessId(branch.getBusiness().getId())
                .address(branch.getAddress())
                .coordinates(branch.getCoordinates())
                .name(branch.getName())
                .overview(branch.getOverview())
                .score(branch.getScore())
                .capacity(branch.getCapacity())
                .reservationPrice(branch.getReservationPrice())
                .reserveOff(branch.getReserveOff())
                .build();

        return dto;
    }

    public FavoriteBranch createFavoriteBranch(Client client, Branch branch) {
        if (client == null) {
            client = createClient(null);
        }
        if (branch == null) {
            branch = createBranch(null);
        }

        FavoriteBranch favoriteBranch = FavoriteBranch.builder()
                .id(ThreadLocalRandom.current().nextLong(999999999))
                .client(client)
                .branch(branch)
                .build();

        if (favoriteBranchRepository != null) {
            favoriteBranch = favoriteBranchRepository.save(favoriteBranch);
        }

        return favoriteBranch;
    }

    public Amenity createAmenity() {
        Amenity amenity = Amenity.builder()
                .id(ThreadLocalRandom.current().nextLong(999999999))
                .name("test")
                .build();

        if (amenityRepository != null) {
            amenity = amenityRepository.save(amenity);
        }

        return amenity;
    }

    public AmenityDTO createAmenityDTO(Amenity amenity) {
        if (amenity == null) {
            amenity = createAmenity();
        }
        AmenityDTO dto = AmenityDTO.builder()
                .id(amenity.getId())
                .name(amenity.getName())
                .build();

        return dto;
    }

    public BranchAmenity createBranchAmenity(Branch branch, Amenity amenity) {
        if (branch == null) {
            branch = createBranch(null);
        }
        if (amenity == null) {
            amenity = createAmenity();
        }

        BranchAmenity branchAmenity = BranchAmenity.builder()
                .id(ThreadLocalRandom.current().nextLong(999999999))
                .branch(branch)
                .amenity(amenity)
                .build();

        if (branchAmenityRepository != null) {
            branchAmenity = branchAmenityRepository.save(branchAmenity);
        }

        return branchAmenity;
    }

    public ProductCategory createProductCategory() {
        ProductCategory category = ProductCategory.builder()
                .id(ThreadLocalRandom.current().nextLong(999999999))
                .name("test")
                .build();

        if (productCategoryRepository != null) {
            category = productCategoryRepository.save(category);
        }

        return category;
    }

    public ProductSubCategory createProductSubCategory(Branch branch, ProductCategory category) {
        if (branch == null) {
            branch = createBranch(null);
        }
        if (category == null) {
            category = createProductCategory();
        }

        ProductSubCategory subCategory = ProductSubCategory.builder()
                .id(ThreadLocalRandom.current().nextLong(999999999))
                .branch(branch)
                .category(category)
                .name("test")
                .build();

        if (productSubCategoryRepository != null) {
            subCategory = productSubCategoryRepository.save(subCategory);
        }

        return subCategory;
    }

    public Review createReview(Client client, Branch branch) {
        if (client == null) {
            client = createClient(null);
        }
        if (branch == null) {
            branch = createBranch(null);
        }

        Review review = Review.builder()
                .id(ThreadLocalRandom.current().nextLong(999999999))
                .client(client)
                .branch(branch)
                .text("test text")
                .date(new Date(System.currentTimeMillis()))
                .build();

        if (reviewRepository != null) {
            review = reviewRepository.save(review);
        }

        return review;
    }

    public ReviewDTO createReviewDTO(Review review) {
        if (review == null) {
            review = createReview(null, null);
        }
        ReviewDTO dto = ReviewDTO.builder()
                .id(review.getId())
                .clientId(review.getClient().getId())
                .branchId(review.getBranch().getId())
                .text(review.getText())
                .date(review.getDate())
                .build();

        return dto;
    }

    public ReviewLike createReviewLike(Client client, Review review) {
        if (client == null) {
            client = createClient(null);
        }
        if (review == null) {
            review = createReview(null, null);
        }

        ReviewLike like = ReviewLike.builder()
                .id(ThreadLocalRandom.current().nextLong(999999999))
                .client(client)
                .review(review)
                .build();

        if (reviewLikeRepository != null) {
            like = reviewLikeRepository.save(like);
        }

        return like;
    }

    public Reservation createReservation(Branch branch) {
        if (branch == null) {
            branch = createBranch(null);
        }

        Reservation reservation = Reservation.builder()
                .id(ThreadLocalRandom.current().nextLong(999999999))
                .branch(branch)
                .requestDate(new Date(System.currentTimeMillis()))
                .reservationDate(new Date(System.currentTimeMillis()))
                .clientNumber(5)
                .payment("payment_test")
                .status(0)
                .payDate(new Date(System.currentTimeMillis()))
                .price(5.0f)
                .build();

        if (reservationRepository != null) {
            reservation = reservationRepository.save(reservation);
        }

        return reservation;
    }

    public ReservationDTO createReservationDTO(Reservation reservation) {
        if (reservation == null) {
            reservation = createReservation(null);
        }
        ReservationDTO dto = ReservationDTO.builder()
            .id(reservation.getId())
            .branchId(reservation.getBranch().getId())
            .requestDate(reservation.getRequestDate())
            .reservationDate(reservation.getReservationDate())
            .clientNumber(reservation.getClientNumber())
            .payment(reservation.getPayment())
            .status(reservation.getStatus())
            .payDate(reservation.getPayDate())
            .price(reservation.getPrice())
                .build();
            
        return dto;
    }
}
