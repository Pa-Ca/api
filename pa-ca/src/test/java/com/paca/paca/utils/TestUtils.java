package com.paca.paca.utils;

import java.util.List;
import java.util.Date;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;

import com.paca.paca.user.model.Role;
import com.paca.paca.user.model.User;
import com.paca.paca.statics.UserRole;
import com.paca.paca.business.model.Tier;
import com.paca.paca.auth.dto.LogoutDTO;
import com.paca.paca.client.model.Client;
import com.paca.paca.client.model.Friend;
import com.paca.paca.client.model.Review;
import com.paca.paca.branch.model.Branch;
import com.paca.paca.statics.BusinessTier;
import com.paca.paca.branch.dto.BranchDTO;
import com.paca.paca.branch.dto.DefaultTaxDTO;
import com.paca.paca.branch.dto.TableDTO;
import com.paca.paca.branch.model.Amenity;
import com.paca.paca.client.dto.ClientDTO;
import com.paca.paca.client.dto.FriendDTO;
import com.paca.paca.client.dto.ReviewDTO;
import com.paca.paca.branch.dto.AmenityDTO;
import com.paca.paca.product.model.Product;
import com.paca.paca.product.dto.ProductDTO;
import com.paca.paca.reservation.model.Guest;
import com.paca.paca.client.model.ReviewLike;
import com.paca.paca.business.model.Business;
import com.paca.paca.user.dto.UserRequestDTO;
import com.paca.paca.business.dto.BusinessDTO;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializers.Default;
import com.paca.paca.auth.dto.LoginRequestDTO;
import com.paca.paca.user.dto.UserResponseDTO;
import com.paca.paca.reservation.dto.GuestDTO;
import com.paca.paca.promotion.model.Promotion;
import com.paca.paca.auth.dto.LoginResponseDTO;
import com.paca.paca.auth.dto.SignupRequestDTO;
import com.paca.paca.auth.dto.RefreshRequestDTO;
import com.paca.paca.promotion.dto.PromotionDTO;
import com.paca.paca.branch.model.BranchAmenity;
import com.paca.paca.branch.model.DefaultTax;
import com.paca.paca.branch.model.Table;
import com.paca.paca.auth.dto.RefreshResponseDTO;
import com.paca.paca.client.model.FavoriteBranch;
import com.paca.paca.reservation.model.ClientGroup;
import com.paca.paca.reservation.model.Reservation;
import com.paca.paca.reservation.dto.ReservationDTO;
import com.paca.paca.user.repository.RoleRepository;
import com.paca.paca.user.repository.UserRepository;
import com.paca.paca.business.repository.TierRepository;
import com.paca.paca.client.repository.ClientRepository;
import com.paca.paca.client.repository.FriendRepository;
import com.paca.paca.client.repository.ReviewRepository;
import com.paca.paca.branch.repository.BranchRepository;
import com.paca.paca.branch.repository.DefaultTaxRepository;
import com.paca.paca.branch.repository.TableRepository;
import com.paca.paca.branch.statics.DefaultTaxStatics;
import com.paca.paca.branch.repository.AmenityRepository;
import com.paca.paca.product.repository.ProductRepository;
import com.paca.paca.reservation.dto.ReservationPaymentDTO;
import com.paca.paca.reservation.repository.GuestRepository;
import com.paca.paca.client.repository.ReviewLikeRepository;
import com.paca.paca.business.repository.BusinessRepository;
import com.paca.paca.promotion.repository.PromotionRepository;
import com.paca.paca.branch.repository.BranchAmenityRepository;
import com.paca.paca.client.repository.FavoriteBranchRepository;
import com.paca.paca.product_sub_category.model.ProductCategory;
import com.paca.paca.product_sub_category.dto.ProductCategoryDTO;
import com.paca.paca.reservation.repository.ClientGroupRepository;
import com.paca.paca.reservation.repository.ReservationRepository;
import com.paca.paca.sale.model.Sale;
import com.paca.paca.sale.dto.SaleDTO;
import com.paca.paca.sale.model.SaleProduct;
import com.paca.paca.sale.model.Tax;
import com.paca.paca.sale.dto.SaleProductDTO;
import com.paca.paca.sale.dto.TaxDTO;
import com.paca.paca.sale.repository.SaleProductRepository;
import com.paca.paca.sale.repository.SaleRepository;
import com.paca.paca.sale.statics.SaleStatics;
import com.paca.paca.sale.statics.TaxStatics;
import com.paca.paca.product_sub_category.model.ProductSubCategory;
import com.paca.paca.product_sub_category.dto.ProductSubCategoryDTO;
import com.paca.paca.product_sub_category.repository.ProductCategoryRepository;
import com.paca.paca.product_sub_category.repository.ProductSubCategoryRepository;

import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;

@Setter
@Getter
@Builder
public class TestUtils {

    RoleRepository roleRepository;

    UserRepository userRepository;

    TierRepository tierRepository;

    GuestRepository guestRepository;

    ClientRepository clientRepository;

    FriendRepository friendRepository;

    BranchRepository branchRepository;

    ReviewRepository reviewRepository;

    AmenityRepository amenityRepository;

    ProductRepository productRepository;

    BusinessRepository businessRepository;

    TableRepository tableRepository;

    DefaultTaxRepository defaultTaxRepository;

    SaleRepository saleRepository;

    SaleProductRepository saleProductRepository;

    PromotionRepository promotionRepository;

    ReviewLikeRepository reviewLikeRepository;

    ClientGroupRepository clientGroupRepository;

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

    public static <T> Page<T> castPage(Class<? extends T> clazz, Page<?> rawCollection) {
        List<T> result = new ArrayList<>(rawCollection.getContent().size());

        for (int i = 0; i < rawCollection.getContent().size(); i++) {
            result.add(clazz.cast(rawCollection.getContent().get(i)));
        }

        return new PageImpl<>(result);
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

    public SignupRequestDTO createSignupRequestDTO() {
        return SignupRequestDTO.builder()
                .email("test@test.com")
                .password("123456789aA#$")
                .role(UserRole.client.name())
                .build();
    }

    public LoginRequestDTO createLoginRequestDTO() {
        return LoginRequestDTO.builder()
                .email("test@test.com")
                .password("123456789aA#$")
                .build();
    }

    public RefreshRequestDTO createRefreshRequestDTO() {
        return RefreshRequestDTO.builder()
                .refresh("eyJHbHJnbfjbsdfjsdf..._NV787nv_458nf83_4")
                .build();
    }

    public LoginResponseDTO createLoginResponseDTO() {
        return LoginResponseDTO.builder()
                .token("eyJhbGciOiJIUzI1NiJ9..._9L5L9hJXCX4WPgpks")
                .refresh("eyJHbHJnbfjbsdfjsdf..._NV787nv_458nf83_4")
                .id(1L)
                .role(UserRole.client.name())
                .build();
    }

    public RefreshResponseDTO createRefreshResponseDTO() {
        return RefreshResponseDTO.builder()
                .token("eyJhbGciOiJIUzI1NiJ9..._9L5L9hJXCX4WPgpks")
                .build();
    }

    public LogoutDTO createLogoutDTO() {
        return LogoutDTO.builder()
                .token("eyJhbGciOiJIUzI1NiJ9..._9L5L9hJXCX4WPgpks")
                .refresh("eyJHbHJnbfjbsdfjsdf..._NV787nv_458nf83_4")
                .build();
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

    public UserRequestDTO createUserRequestDTO(User user) {
        if (user == null) {
            user = createUser();
        }
        UserRequestDTO dto = UserRequestDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .verified(user.getVerified())
                .loggedIn(user.getLoggedIn())
                .role(user.getRole().getName().name())
                .build();

        return dto;
    }

    public UserResponseDTO createUserResponseDTO(User user) {
        if (user == null) {
            user = createUser();
        }
        UserResponseDTO dto = UserResponseDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
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
                .dateOfBirth(new java.sql.Date(System.currentTimeMillis()))
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

    public Tier createTier(BusinessTier businessTier) {
        Tier tier = Tier.builder()
                .id((long) businessTier.ordinal())
                .name(businessTier)
                .reservationLimit(1)
                .tierCost(BigDecimal.valueOf(1f))
                .build();
        return tier;
    }

    public Tier createTier(BusinessTier businessTier, int reservationLimit, BigDecimal tierCost) {
        Tier tier = Tier.builder()
                .id((long) businessTier.ordinal())
                .name(businessTier)
                .reservationLimit(reservationLimit)
                .tierCost(tierCost)
                .build();
        return tier;
    }

    public Business createBusiness(User user) {
        if (user == null) {
            user = createUser();
        }

        Business business = Business.builder()
                .id(ThreadLocalRandom.current().nextLong(999999999))
                .user(user)
                .name("Test")
                .verified(false)
                .tier(createTier(BusinessTier.basic))
                .phoneNumber("test phone")
                .build();

        if (businessRepository != null) {
            business = businessRepository.save(business);
        }
        return business;
    }

    public Business createBusiness(User user, Tier tier) {
        if (user == null) {
            user = createUser();
        }

        Business business = Business.builder()
                .id(ThreadLocalRandom.current().nextLong(999999999))
                .user(user)
                .name("Test")
                .verified(false)
                .tier(tier)
                .phoneNumber("test phone")
                .build();

        if (businessRepository != null) {
            business = businessRepository.save(business);
        }
        return business;
    }

    public BusinessDTO createBusinessDTO(Business business) {
        if (business == null) {
            business = createBusiness(null);
        }
        BusinessDTO dto = BusinessDTO.builder()
                .id(business.getId())
                .userId(business.getUser().getId())
                .email(business.getUser().getEmail())
                .name(business.getName())
                .verified(business.getVerified())
                .tier(BusinessTier.basic.name())
                .phoneNumber(business.getPhoneNumber())
                .build();
        return dto;
    }

    public Branch createBranch(Business business) {
        if (business == null) {
            business = createBusiness(null);
        }
        Branch branch = Branch.builder()
                .id(ThreadLocalRandom.current().nextLong(999999999))
                .business(business)
                .location("location test")
                .mapsLink("mapsLink test")
                .name("name test")
                .overview("overview test")
                .score(4.0F)
                .capacity(42)
                .reservationPrice(BigDecimal.valueOf(37.0F))
                .reserveOff(false)
                .averageReserveTime(Duration.ofHours(2).plusMinutes(45))
                .visibility(true)
                .phoneNumber("test phone")
                .type("test type")
                .hourIn(LocalTime.of(8, 5, 1))
                .hourOut(LocalTime.of(8, 5, 1))
                .deleted(false)
                .dollarToLocalCurrencyExchange(BigDecimal.valueOf(1.0F))
                .build();

        if (branchRepository != null) {
            branch = branchRepository.save(branch);
        }

        return branch;
    }

    public List<Branch> createTestBranches(Business business) {
        if (business == null) {
            business = createBusiness(null);
        }

        List<Float> scores = new ArrayList<>();
        scores.add(1.0F);
        scores.add(2.0F);
        scores.add(3.5F);
        scores.add(4.2F);
        scores.add(5.0F);

        List<BigDecimal> reservation_prices = new ArrayList<>();
        reservation_prices.add(BigDecimal.valueOf(2.0F));
        reservation_prices.add(BigDecimal.valueOf(20.0F));
        reservation_prices.add(BigDecimal.valueOf(31.0F));
        reservation_prices.add(BigDecimal.valueOf(40.5F));
        reservation_prices.add(BigDecimal.valueOf(49.0F));

        List<String> names = new ArrayList<>();
        names.add("name test 1");
        names.add("name test 2");
        names.add("name test 3");
        names.add("name test 4");
        names.add("name test 5");

        List<Integer> capacities = new ArrayList<>();
        capacities.add(5);
        capacities.add(10);
        capacities.add(15);
        capacities.add(20);
        capacities.add(25);

        List<Branch> branches = new ArrayList<>();

        // Lop through the lists and create branches
        for (int i = 0; i < 5; i++) {
            Branch branch = Branch.builder()
                    .id(ThreadLocalRandom.current().nextLong(999999999))
                    .business(business)
                    .location("location test")
                    .mapsLink("mapsLink test")
                    .name(names.get(i))
                    .overview("overview test")
                    .score(scores.get(i))
                    .capacity(capacities.get(i))
                    .reservationPrice(reservation_prices.get(i))
                    .reserveOff(false)
                    .averageReserveTime(Duration.ofHours(2).plusMinutes(45))
                    .visibility(true)
                    .phoneNumber("test phone")
                    .type("test type")
                    .hourIn(LocalTime.of(8, 5, 1))
                    .hourOut(LocalTime.of(8, 5, 1))
                    .deleted(false)
                    .dollarToLocalCurrencyExchange(BigDecimal.valueOf(1.0F))
                    .build();

            if (branchRepository != null) {
                branch = branchRepository.save(branch);
            }

            branches.add(branch);
        }

        return branches;
    }

    public BranchDTO createBranchDTO(Branch branch) {
        if (branch == null) {
            branch = createBranch(null);
        }
        BranchDTO dto = BranchDTO.builder()
                .id(branch.getId())
                .businessId(branch.getBusiness().getId())
                .location(branch.getLocation())
                .mapsLink(branch.getMapsLink())
                .name(branch.getName())
                .overview(branch.getOverview())
                .score(branch.getScore())
                .capacity(branch.getCapacity())
                .reservationPrice(branch.getReservationPrice())
                .reserveOff(branch.getReserveOff())
                .averageReserveTime(branch.getAverageReserveTime())
                .visibility(branch.getVisibility())
                .phoneNumber(branch.getPhoneNumber())
                .type(branch.getType())
                .hourIn(branch.getHourIn())
                .hourOut(branch.getHourOut())
                .deleted(branch.getDeleted())
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

    public Product createProduct(ProductSubCategory subCategory) {
        if (subCategory == null) {
            subCategory = createProductSubCategory(null, null);
        }
        Product product = Product.builder()
                .id(ThreadLocalRandom.current().nextLong(999999999))
                .subCategory(subCategory)
                .disabled(false)
                .name("test name")
                .price(BigDecimal.valueOf(10.0f))
                .description("test description")
                .build();

        if (productRepository != null) {
            product = productRepository.save(product);
        }

        return product;
    }

    public ProductDTO createProductDTO(Product product) {
        if (product == null) {
            product = createProduct(null);
        }
        ProductDTO dto = ProductDTO.builder()
                .id(product.getId())
                .subCategoryId(product.getSubCategory().getId())
                .disabled(product.getDisabled())
                .name(product.getName())
                .price(product.getPrice())
                .description(product.getDescription())
                .build();

        return dto;
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

    public ProductCategoryDTO createProductCategoryDTO(ProductCategory category) {
        if (category == null) {
            category = createProductCategory();
        }
        ProductCategoryDTO dto = ProductCategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .build();

        return dto;

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

    public ProductSubCategoryDTO createProductSubCategoryDTO(ProductSubCategory subCategory) {
        if (subCategory == null) {
            subCategory = createProductSubCategory(null, null);
        }
        ProductSubCategoryDTO dto = ProductSubCategoryDTO.builder()
                .id(subCategory.getId())
                .branchId(subCategory.getBranch().getId())
                .categoryId(subCategory.getCategory().getId())
                .name(subCategory.getName())
                .build();

        return dto;
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
                .date(new Date())
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

    public Guest createGuest() {
        Guest guest = Guest.builder()
                .id(ThreadLocalRandom.current().nextLong(9999999))
                .name("name_test")
                .surname("surname_test")
                .email("email_test")
                .phoneNumber("phone_number_test")
                .build();

        if (guestRepository != null) {
            guest = guestRepository.save(guest);
        }

        return guest;
    }

    public GuestDTO createGuestDTO(Guest guest) {
        if (guest == null) {
            guest = createGuest();
        }
        GuestDTO dto = GuestDTO.builder()
                .id(guest.getId())
                .name(guest.getName())
                .surname(guest.getSurname())
                .email(guest.getEmail())
                .phoneNumber(guest.getPhoneNumber())
                .build();

        return dto;
    }

    public Reservation createReservation(Branch branch, Guest guest) {
        if (branch == null) {
            branch = createBranch(null);
        }
        if (guest == null) {
            guest = createGuest();
        }

        Reservation reservation = Reservation.builder()
                .id(ThreadLocalRandom.current().nextLong(999999999))
                .branch(branch)
                .guest(guest)
                .requestDate(new Date(System.currentTimeMillis()))
                .reservationDate(new Date(System.currentTimeMillis()))
                .clientNumber(5)
                .payment("payment_test")
                .status(0)
                .payDate(new Date(System.currentTimeMillis()))
                .price(BigDecimal.valueOf(5.0f))
                .occasion("Anniversary")
                .byClient(Boolean.TRUE)
                .build();

        if (reservationRepository != null) {
            reservation = reservationRepository.save(reservation);
        }

        return reservation;
    }

    public Reservation createReservation(Branch branch) {
        if (branch == null) {
            branch = createBranch(null);
        }

        Reservation reservation = Reservation.builder()
                .id(ThreadLocalRandom.current().nextLong(999999999))
                .branch(branch)
                .guest(null)
                .requestDate(new Date(System.currentTimeMillis()))
                .reservationDate(new Date(System.currentTimeMillis()))
                .clientNumber(5)
                .payment("payment_test")
                .status(0)
                .payDate(new Date(System.currentTimeMillis()))
                .price(BigDecimal.valueOf(5.0f))
                .occasion("Anniversary")
                .byClient(Boolean.TRUE)
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
                .guestId(reservation.getGuest() == null ? null : reservation.getGuest().getId())
                .requestDate(reservation.getRequestDate())
                .reservationDate(reservation.getReservationDate())
                .clientNumber(reservation.getClientNumber())
                .payment(reservation.getPayment())
                .status(reservation.getStatus())
                .payDate(reservation.getPayDate())
                .price(reservation.getPrice())
                .occasion(reservation.getOccasion())
                .byClient(reservation.getByClient())
                .haveGuest(reservation.getGuest() != null)
                .build();

        return dto;
    }

    public Promotion createPromotion(Branch branch) {
        if (branch == null) {
            branch = createBranch(null);
        }

        Promotion promotion = Promotion.builder()
                .id(ThreadLocalRandom.current().nextLong(999999999))
                .branch(branch)
                .disabled(false)
                .text("text test")
                .build();

        if (promotionRepository != null) {
            promotion = promotionRepository.save(promotion);
        }

        return promotion;
    }

    public PromotionDTO createPromotionDTO(Promotion promotion) {
        if (promotion == null) {
            promotion = createPromotion(null);
        }

        PromotionDTO dto = PromotionDTO.builder()
                .id(promotion.getId())
                .branchId(promotion.getBranch().getId())
                .disabled(promotion.getDisabled())
                .text(promotion.getText())
                .build();
        return dto;
    }

    public ClientGroup createClientGroup(Client client, Reservation reservation) {
        if (client == null) {
            client = createClient(null);
        }
        if (reservation == null) {
            reservation = createReservation(null);
        }
        ClientGroup clientGroup = ClientGroup.builder()
                .id(ThreadLocalRandom.current().nextLong(999999999))
                .client(client)
                .reservation(reservation)
                .name("Community")
                .build();

        if (clientGroupRepository != null) {
            clientGroup = clientGroupRepository.save(clientGroup);
        }
        return clientGroup;
    }

    public ReservationPaymentDTO createReservationPaymentDTO(String paymentCode) {
        if (paymentCode == null) {
            paymentCode = "paymentCode69";
        }
        ReservationPaymentDTO dto = ReservationPaymentDTO.builder()
                .paymentCode(paymentCode)
                .build();
        return dto;
    }

    public List<Reservation> createTestReservations(Branch branch_1,
            Branch branch_2,
            Date date_1,
            Date date_2) {
        /*
         * Creates test reservations for testing the ReservationService
         * It creates two branches, two dates, and 10 reservations for each branch and
         * date
         */
        //
        List<Reservation> reservations = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            Branch branch;
            Date date;
            if (i % 2 == 0) {
                branch = branch_1;
                date = date_1;
            } else {
                branch = branch_2;
                date = date_2;
            }
            Reservation reservation = Reservation.builder()
                    .id(ThreadLocalRandom.current().nextLong(999999999))
                    .branch(branch)
                    .guest(null)
                    .requestDate(new Date(System.currentTimeMillis()))
                    .reservationDate(date)
                    .clientNumber(5)
                    .payment("payment_test")
                    .status(0)
                    .payDate(new Date(System.currentTimeMillis()))
                    .price(BigDecimal.valueOf(5.0f))
                    .occasion("Anniversary")
                    .byClient(Boolean.TRUE)
                    .build();

            if (branchRepository != null) {
                reservation = reservationRepository.save(reservation);
            }

            reservations.add(reservation);
        }

        return reservations;
    }

    // Create a new Tax
    public Tax createTax(Sale sale){
        
        if (sale == null) {
            sale = createSale(null, null);
        }

        Tax tax = Tax.builder()
                .id(ThreadLocalRandom.current().nextLong(999999999))
                .sale(sale)
                .name("Tax")
                .type(TaxStatics.Types.PERCENTAGE)
                .value(50.f)
                .build();

        return tax;
    }

    // Create a new DefaultTax
    public DefaultTax createDefaultTax(Branch branch){
        
        // If the branch is null, create a new one
        if (branch == null) {
            branch = createBranch(null);
        }

        DefaultTax defaultTax = DefaultTax.builder()
                .id(ThreadLocalRandom.current().nextLong(999999999))
                .branch(branch)
                .name("Default Tax")
                .type(DefaultTaxStatics.Types.PERCENTAGE)
                .build();

        if (defaultTaxRepository != null) {
            defaultTax = defaultTaxRepository.save(defaultTax);
        }

        return defaultTax;
    }

    public Table createTable(Branch branch){
        if (branch == null) {
            branch = createBranch(null);
        }

        Table table = Table.builder()
                .id(ThreadLocalRandom.current().nextLong(999999999))
                .branch(branch)
                .name("Table 1")
                .deleted(false)
                .build();

        if (tableRepository != null) {
            table = tableRepository.save(table);
        }

        return table;
    }


    public Sale createSale(Table table, Reservation reservation){
        
        // If the table is null, create a new one
        if (table == null) {
            table = createTable(null);
        }

        // If the reservation is null, create a new one
        if (reservation == null) {
            reservation = createReservation(null);
        }

        Sale sale = Sale.builder()
                .id(ThreadLocalRandom.current().nextLong(999999999))
                .table(table)
                .tableName(table.getName())
                .startTime(new Date(System.currentTimeMillis()))
                .endTime(null)
                .status(SaleStatics.Status.ongoing)
                .reservation(reservation)
                .build();
        
        if (saleRepository != null) {
            sale = saleRepository.save(sale);
        }
        return sale;
    }

    public SaleProduct createSaleProduct(Sale sale, Product product){
        if (sale == null) {
            sale = createSale(null, null);
        }

        if (product == null) {
            product = createProduct(null);
        }


        SaleProduct saleProduct = SaleProduct.builder()
                .id(ThreadLocalRandom.current().nextLong(999999999))
                .sale(sale)
                .name(product.getName())
                .price(BigDecimal.valueOf(5.0f))
                .ammount(5)
                .product(product)
                .build();

        if (saleProductRepository != null) {
            saleProduct = saleProductRepository.save(saleProduct);
        }

        return saleProduct;
    }

    public SaleDTO createSaleDTO(Table table, Reservation reservation){
        
        // If the table is null, create a new one
        if (table == null) {
            table = createTable(null);
        }

        // If the reservation is null, create a new one
        if (reservation == null) {
            reservation = createReservation(null);
        }

        SaleDTO dto = SaleDTO.builder()
                .id(ThreadLocalRandom.current().nextLong(999999999))
                .tableId(table.getId())
                .tableName(table.getName())
                .startTime(new Date(System.currentTimeMillis()))
                .endTime(null)
                .status(SaleStatics.Status.ongoing)
                .reservationId(reservation.getId())
                .build();
        
        return dto;
    }

    public SaleProductDTO createSaleProductDTO(Sale sale, Product product){
        if (sale == null) {
            sale = createSale(null, null);
        }

        if (product == null) {
            product = createProduct(null);
        }

        SaleProductDTO dto = SaleProductDTO.builder()
                .id(ThreadLocalRandom.current().nextLong(999999999))
                .saleId(sale.getId())
                .name(product.getName())
                .price(BigDecimal.valueOf(5.0f))
                .ammount(5)
                .productId(product.getId())
                .build();
        

        return dto;
    }

    public TableDTO createTableDTO(Branch branch){
        if (branch == null) {
            branch = createBranch(null);
        }

        TableDTO dto = TableDTO.builder()
                .id(ThreadLocalRandom.current().nextLong(999999999))
                .branchId(branch.getId())
                .name("Table 1")
                .deleted(false)
                .build();

        return dto;
    }

    public DefaultTaxDTO createDefaultTaxDTO(Branch branch){

        // If the branch is null, create a new one
        if (branch == null) {
            branch = createBranch(null);
        }

        DefaultTaxDTO dto = DefaultTaxDTO.builder()
                .id(ThreadLocalRandom.current().nextLong(999999999))
                .branchId(branch.getId())
                .name("Default Tax")
                .type(DefaultTaxStatics.Types.PERCENTAGE)
                .value(5.0f)
                .build();

        return dto;
    }

    public TaxDTO createTaxDTO(Sale sale){

        // If the branch is null, create a new one
        if (sale == null) {
            sale = createSale(null, null);
        }

        TaxDTO dto = TaxDTO.builder()
                .id(ThreadLocalRandom.current().nextLong(999999999))
                .saleId(sale.getId())
                .name("Tax")
                .type(TaxStatics.Types.PERCENTAGE)
                .value(5.0f)
                .build();

        return dto;
    }

}
