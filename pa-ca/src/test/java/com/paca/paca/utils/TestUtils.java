package com.paca.paca.utils;

import java.util.List;
import java.util.Random;
import java.util.Date;
import java.util.UUID;
import java.util.Calendar;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;

import com.paca.paca.sale.model.Tax;
import com.paca.paca.sale.dto.TaxDTO;
import com.paca.paca.user.model.Role;
import com.paca.paca.user.model.User;
import com.paca.paca.sale.model.InsiteSale;
import com.paca.paca.sale.model.InsiteSaleTable;
import com.paca.paca.sale.model.Sale;
import com.paca.paca.sale.dto.SaleDTO;
import com.paca.paca.statics.UserRole;
import com.paca.paca.auth.dto.LogoutDTO;
import com.paca.paca.branch.model.Table;
import com.paca.paca.business.model.Tier;
import com.paca.paca.client.model.Client;
import com.paca.paca.client.model.ClientGuest;
import com.paca.paca.client.model.Friend;
import com.paca.paca.client.model.Review;
import com.paca.paca.branch.model.Branch;
import com.paca.paca.branch.dto.TableDTO;
import com.paca.paca.statics.BusinessTier;
import com.paca.paca.branch.dto.BranchDTO;
import com.paca.paca.branch.dto.DefaultTaxDTO;
import com.paca.paca.branch.model.Amenity;
import com.paca.paca.client.dto.ClientDTO;
import com.paca.paca.client.dto.FriendDTO;
import com.paca.paca.client.dto.ReviewDTO;
import com.paca.paca.branch.dto.AmenityDTO;
import com.paca.paca.product.model.Product;
import com.paca.paca.sale.model.SaleProduct;
import com.paca.paca.sale.model.SaleTax;
import com.paca.paca.product.dto.ProductDTO;
import com.paca.paca.sale.dto.SaleProductDTO;
import com.paca.paca.reservation.model.Guest;
import com.paca.paca.reservation.model.Invoice;
import com.paca.paca.client.model.ReviewLike;
import com.paca.paca.business.model.Business;
import com.paca.paca.user.dto.UserRequestDTO;
import com.paca.paca.branch.model.Tax;
import com.paca.paca.branch.model.PaymentOption;
import com.paca.paca.sale.statics.TaxStatics;
import com.paca.paca.sale.statics.SaleStatics;
import com.paca.paca.business.dto.BusinessDTO;
import com.paca.paca.auth.dto.LoginRequestDTO;
import com.paca.paca.user.dto.UserResponseDTO;
import com.paca.paca.branch.dto.TaxDTO;
import com.paca.paca.branch.dto.PaymentOptionDTO;
import com.paca.paca.reservation.dto.GuestDTO;
import com.paca.paca.reservation.dto.InvoiceDTO;
import com.paca.paca.promotion.model.Promotion;
import com.paca.paca.auth.dto.LoginResponseDTO;
import com.paca.paca.auth.dto.SignupRequestDTO;
import com.paca.paca.auth.dto.RefreshRequestDTO;
import com.paca.paca.promotion.dto.PromotionDTO;
import com.paca.paca.branch.model.BranchAmenity;
import com.paca.paca.branch.model.DefaultTax;
import com.paca.paca.auth.dto.RefreshResponseDTO;
import com.paca.paca.client.model.FavoriteBranch;
import com.paca.paca.reservation.model.ClientGroup;
import com.paca.paca.reservation.model.Reservation;
import com.paca.paca.reservation.dto.ReservationDTO;
import com.paca.paca.user.repository.RoleRepository;
import com.paca.paca.user.repository.UserRepository;
import com.paca.paca.sale.repository.SaleRepository;
import com.paca.paca.sale.repository.SaleTaxRepository;
import com.paca.paca.branch.statics.DefaultTaxStatics;
import com.paca.paca.branch.repository.TableRepository;
import com.paca.paca.business.repository.TierRepository;
import com.paca.paca.client.repository.ClientGuestRepository;
import com.paca.paca.client.repository.ClientRepository;
import com.paca.paca.client.repository.FriendRepository;
import com.paca.paca.client.repository.ReviewRepository;
import com.paca.paca.branch.repository.BranchRepository;
import com.paca.paca.branch.repository.AmenityRepository;
import com.paca.paca.product.repository.ProductRepository;
import com.paca.paca.reservation.dto.ReservationPaymentDTO;
import com.paca.paca.sale.repository.InsiteSaleRepository;
import com.paca.paca.sale.repository.InsiteSaleTableRepository;
import com.paca.paca.sale.repository.SaleProductRepository;
import com.paca.paca.branch.repository.DefaultTaxRepository;
import com.paca.paca.branch.repository.PaymentOptionRepository;
import com.paca.paca.reservation.repository.GuestRepository;
import com.paca.paca.reservation.repository.InvoiceRepository;
import com.paca.paca.client.repository.ReviewLikeRepository;
import com.paca.paca.business.repository.BusinessRepository;
import com.paca.paca.productSubCategory.model.ProductCategory;
import com.paca.paca.promotion.repository.PromotionRepository;
import com.paca.paca.branch.repository.BranchAmenityRepository;
import com.paca.paca.productSubCategory.dto.ProductCategoryDTO;
import com.paca.paca.client.repository.FavoriteBranchRepository;
import com.paca.paca.productSubCategory.model.ProductSubCategory;
import com.paca.paca.productSubCategory.dto.ProductSubCategoryDTO;
import com.paca.paca.reservation.repository.ClientGroupRepository;
import com.paca.paca.reservation.repository.ReservationRepository;
import com.paca.paca.reservation.statics.ReservationStatics;
import com.paca.paca.productSubCategory.repository.ProductCategoryRepository;
import com.paca.paca.productSubCategory.repository.ProductSubCategoryRepository;

import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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

    TierRepository tierRepository;

    SaleRepository saleRepository;

    TableRepository tableRepository;

    GuestRepository guestRepository;

    ClientRepository clientRepository;

    FriendRepository friendRepository;

    BranchRepository branchRepository;

    ReviewRepository reviewRepository;

    InvoiceRepository invoiceRepository;

    AmenityRepository amenityRepository;

    ProductRepository productRepository;

    BusinessRepository businessRepository;

    PromotionRepository promotionRepository;

    InsiteSaleRepository insiteSaleRepository;

    DefaultTaxRepository defaultTaxRepository;

    ReviewLikeRepository reviewLikeRepository;

    SaleProductRepository saleProductRepository;

    ClientGroupRepository clientGroupRepository;

    ClientGuestRepository clientGuestRepository;

    ReservationRepository reservationRepository;

    SaleTaxRepository saleTaxRepository;

    BranchAmenityRepository branchAmenityRepository;

    PaymentOptionRepository paymentOptionRepository;

    FavoriteBranchRepository favoriteBranchRepository;

    InsiteSaleTableRepository insiteSaleTableRepository;

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
                .dateOfBirth(new java.util.Date(System.currentTimeMillis()))
                .identityDocument("V" + ThreadLocalRandom.current().nextLong(99999999))
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
                .identityDocument(client.getIdentityDocument())
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
                .name("name_test_" + UUID.randomUUID().toString())
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
                .name("test_name_" + UUID.randomUUID().toString())
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
                .name("test_" + UUID.randomUUID().toString())
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
                .identityDocument("iden_doc_test")
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
                .identityDocument(guest.getIdentityDocument())
                .build();

        return dto;
    }

    public Reservation createReservation(Branch branch) {
        if (branch == null) {
            branch = createBranch(null);
        }

        Reservation reservation = Reservation.builder()
                .id(ThreadLocalRandom.current().nextLong(999999999))
                .branch(branch)
                .guest(null)
                .invoice(null)
                .requestDate(new Date(System.currentTimeMillis()))
                .reservationDateIn(new Date(System.currentTimeMillis()))
                .reservationDateOut(new Date(System.currentTimeMillis()))
                .clientNumber(5)
                .tableNumber(2)
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
                .invoice(null)
                .requestDate(new Date(System.currentTimeMillis()))
                .reservationDateIn(new Date(System.currentTimeMillis()))
                .reservationDateOut(new Date(System.currentTimeMillis()))
                .clientNumber(5)
                .tableNumber(2)
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

    public Reservation createReservation(Branch branch, Guest guest, Invoice invoice) {
        if (branch == null) {
            branch = createBranch(null);
        }
        if (guest == null) {
            guest = createGuest();
        }
        if (invoice == null) {
            invoice = createInvoice(null, null);
        }

        Reservation reservation = Reservation.builder()
                .id(ThreadLocalRandom.current().nextLong(999999999))
                .branch(branch)
                .guest(guest)
                .invoice(invoice)
                .requestDate(new Date(System.currentTimeMillis()))
                .reservationDateIn(new Date(System.currentTimeMillis()))
                .reservationDateOut(new Date(System.currentTimeMillis()))
                .clientNumber(5)
                .tableNumber(2)
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
                .reservationDateIn(reservation.getReservationDateIn())
                .reservationDateOut(reservation.getReservationDateOut())
                .clientNumber(reservation.getClientNumber())
                .tableNumber(reservation.getTableNumber())
                .payment(reservation.getPayment())
                .status(reservation.getStatus())
                .payDate(reservation.getPayDate())
                .price(reservation.getPrice())
                .occasion(reservation.getOccasion())
                .byClient(reservation.getByClient())
                .haveGuest(reservation.getGuest() != null)
                .name(reservation.getGuest() == null ? null : reservation.getGuest().getName())
                .surname(reservation.getGuest() == null ? null : reservation.getGuest().getSurname())
                .email(reservation.getGuest() == null ? null : reservation.getGuest().getEmail())
                .phoneNumber(reservation.getGuest() == null ? null : reservation.getGuest().getPhoneNumber())
                .identityDocument(reservation.getGuest() == null ? null : reservation.getGuest().getIdentityDocument())
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
                .isOwner(true)
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
                    .reservationDateIn(date)
                    .reservationDateOut(date)
                    .clientNumber(5)
                    .tableNumber(2)
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
    public Tax createTax() {

        Tax tax = Tax.builder()
                .id(ThreadLocalRandom.current().nextLong(999999999))
                .name("Tax")
                .type(TaxStatics.Types.PERCENTAGE)
                .value(50.f)
                .build();

        return tax;
    }

    // Create a new DefaultTax
    public DefaultTax createDefaultTax(Tax tax, Branch branch) {
        if (tax == null) {
            tax = createTax();
        }
        if (branch == null) {
            branch = createBranch(null);
        }

        DefaultTax defaultTax = DefaultTax.builder()
                .id(ThreadLocalRandom.current().nextLong(999999999))
                .tax(tax)
                .branch(branch)
                .build();

        if (defaultTaxRepository != null) {
            defaultTax = defaultTaxRepository.save(defaultTax);
        }

        return defaultTax;
    }

    public Table createTable(Branch branch) {
        if (branch == null) {
            branch = createBranch(null);
        }

        Table table = Table.builder()
                .id(ThreadLocalRandom.current().nextLong(999999999))
                .branch(branch)
                .name("Table_" + UUID.randomUUID().toString())
                .deleted(false)
                .build();

        if (tableRepository != null) {
            table = tableRepository.save(table);
        }

        return table;
    }

    public PaymentOption createPaymentOption(Branch branch) {
        if (branch == null) {
            branch = createBranch(null);
        }

        PaymentOption paymentOption = PaymentOption.builder()
                .id(ThreadLocalRandom.current().nextLong(999999999))
                .branch(branch)
                .name("PaymentOption_" + UUID.randomUUID().toString())
                .build();

        if (paymentOptionRepository != null) {
            paymentOption = paymentOptionRepository.save(paymentOption);
        }

        return paymentOption;
    }

    public Sale createSale(Branch branch, ClientGuest clientGuest, Invoice invoice) {
        if (branch == null) {
            branch = createBranch(null);
        }
        if (clientGuest == null) {
            clientGuest = createClientGuest((Client) null);
        }
        if (invoice == null) {
            invoice = createInvoice();
        }

        Sale sale = new Sale(1);

        if (saleRepository != null) {
            sale = saleRepository.save(sale);
        }
        return sale;
    }

    public SaleProduct createSaleProduct(Sale sale, Product product) {
        if (sale == null) {
            sale = createSale(null, null, null);
        }

        if (product == null) {
            product = createProduct(null);
        }

        SaleProduct saleProduct = SaleProduct.builder()
                .id(ThreadLocalRandom.current().nextLong(999999999))
                .sale(sale)
                .name(product.getName())
                .price(BigDecimal.valueOf(5.0f))
                .amount(5)
                .product(product)
                .build();

        if (saleProductRepository != null) {
            saleProduct = saleProductRepository.save(saleProduct);
        }

        return saleProduct;
    }

    public SaleDTO createSaleDTO(Sale sale) {
        if (sale == null) {
            sale = createSale(null, null, null);
        }

        SaleDTO dto = SaleDTO.builder()
                .id(ThreadLocalRandom.current().nextLong(999999999))
                .tableId(table.getId())
                .tableName(table.getName())
                .startTime(new Date(System.currentTimeMillis()))
                .status(SaleStatics.Status.ongoing)
                .reservationId(reservation.getId())
                .build();

        return dto;
    }

    public SaleProductDTO createSaleProductDTO(SaleProduct saleProduct) {
        if (saleProduct == null) {
            saleProduct = createSaleProduct(null, null);
        }

        SaleProductDTO dto = SaleProductDTO.builder()
                .id(ThreadLocalRandom.current().nextLong(999999999))
                .saleId(sale.getId())
                .name(product.getName())
                .price(BigDecimal.valueOf(5.0f))
                .amount(5)
                .productId(product.getId())
                .build();

        return dto;
    }

    public TableDTO createTableDTO(Branch branch) {
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

    public PaymentOptionDTO createPaymentOptionDTO(Branch branch) {
        if (branch == null) {
            branch = createBranch(null);
        }

        PaymentOptionDTO dto = PaymentOptionDTO.builder()
                .id(ThreadLocalRandom.current().nextLong(999999999))
                .branchId(branch.getId())
                .name("PaymentOption 1")
                .build();

        return dto;
    }

    public DefaultTaxDTO createDefaultTaxDTO(DefaultTax defaultTax) {
        if (defaultTax == null) {
            defaultTax = createDefaultTax(null, null);
        }

        DefaultTaxDTO dto = DefaultTaxDTO.builder()
                .id(ThreadLocalRandom.current().nextLong(999999999))
                .branchId(defaultTax.getBranch().getId())
                .tax(createTaxDTO(defaultTax.getTax()))
                .build();

        return dto;
    }

    public TaxDTO createTaxDTO(Tax tax) {
        if (tax == null) {
            tax = createTax();
        }

        TaxDTO dto = TaxDTO.builder()
                .id(tax.getId())
                .type(tax.getType())
                .name(tax.getName())
                .value(tax.getValue())
                .build();

        return dto;
    }

    public List<Sale> createTestSales(
            List<Branch> branches,
            Date minDate,
            Date maxDate,
            List<String> names,
            List<String> surnames,
            List<String> identityDocuments) {
        Random rand = new Random();

        // Create random guests
        List<Guest> guests = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Guest guest = new Guest(
                    ThreadLocalRandom.current().nextLong(999999999),
                    names.get(rand.nextInt(names.size())) + UUID.randomUUID().toString(),
                    surnames.get(rand.nextInt(names.size())) + UUID.randomUUID().toString(),
                    "email_test_" + UUID.randomUUID().toString(),
                    "phone_number_test_" + UUID.randomUUID().toString(),
                    identityDocuments.get(rand.nextInt(names.size())) + UUID.randomUUID().toString());
            if (guestRepository != null) {
                guest = guestRepository.save(guest);
            }
            guests.add(guest);
        }

        // Create random clients
        List<Client> clients = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            User user = createUser();
            Client client = new Client(
                    ThreadLocalRandom.current().nextLong(999999999),
                    user,
                    names.get(rand.nextInt(names.size())) + UUID.randomUUID().toString(),
                    surnames.get(rand.nextInt(names.size())) + UUID.randomUUID().toString(),
                    identityDocuments.get(rand.nextInt(names.size())) + UUID.randomUUID().toString(),
                    "address_test_" + UUID.randomUUID().toString(),
                    "phone_number_test_" + UUID.randomUUID().toString(),
                    "stripe_id_test_" + UUID.randomUUID().toString(),
                    new Date(System.currentTimeMillis()));
            if (clientRepository != null) {
                client = clientRepository.save(client);
            }
            clients.add(client);
        }

        // Create random sales
        List<Sale> sales = new ArrayList<>();
        for (int i = 0; i < 5000; i++) {
            Date date = new Date(ThreadLocalRandom.current().nextLong(
                    minDate.getTime(),
                    maxDate.getTime()));
            Sale sale = new Sale(
                    ThreadLocalRandom.current().nextLong(999999999),
                    branches.get(rand.nextInt(branches.size())),
                    null,
                    null,
                    Short.valueOf("1"),
                    SaleStatics.Status.ALL.get(rand.nextInt(SaleStatics.Status.ALL.size())),
                    date,
                    new Date(date.getTime() + 1000 * 60 * 60),
                    1.0F,
                    "note_test_" + UUID.randomUUID().toString());

            ClientGuest clientGuest = new ClientGuest(
                    ThreadLocalRandom.current().nextLong(999999999),
                    null,
                    null,
                    null);
            if (rand.nextDouble() < 0.5) {
                // Sale with guest
                clientGuest.setGuest(guests.get(rand.nextInt(guests.size())));
                clientGuest.setHaveGuest(Boolean.TRUE);
            } else {
                // Sale with client
                clientGuest.setClient(clients.get(rand.nextInt(clients.size())));
                clientGuest.setHaveGuest(Boolean.FALSE);
            }
            sale.setClientGuest(clientGuest);

            if (clientGuestRepository != null) {
                clientGuest = clientGuestRepository.save(clientGuest);
            }
            if (saleRepository != null) {
                sale = saleRepository.save(sale);
            }
            sales.add(sale);
        }

        return sales;
    }

    public Invoice createInvoice() {
        Invoice invoice = new Invoice(
                ThreadLocalRandom.current().nextLong(999999999),
                new Date(System.currentTimeMillis()),
                BigDecimal.valueOf(10.0f),
                "payment test",
                UUID.randomUUID().toString());

        if (invoiceRepository != null) {
            invoice = invoiceRepository.save(invoice);
        }
        return invoice;
    }

    public InvoiceDTO createInvoiceDTO(Invoice invoice) {
        if (invoice == null) {
            invoice = createInvoice(null);
        }

        InvoiceDTO dto = new InvoiceDTO(
                invoice.getId(),
                invoice.getReservation().getId(),
                invoice.getPayDate(),
                invoice.getPrice(),
                invoice.getPayment(),
                invoice.getPaymentCode());

        return dto;
    }

    public List<Reservation> createTestReservations(
            List<Branch> branches,
            Date minDate,
            Date maxDate,
            List<String> names,
            List<String> surnames,
            List<String> identityDocuments) {
        Random rand = new Random();

        // Create random guests
        List<Guest> guests = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Guest guest = new Guest(
                    ThreadLocalRandom.current().nextLong(999999999),
                    names.get(rand.nextInt(names.size())) + UUID.randomUUID().toString(),
                    surnames.get(rand.nextInt(names.size())) + UUID.randomUUID().toString(),
                    "email_test_" + UUID.randomUUID().toString(),
                    "phone_number_test_" + UUID.randomUUID().toString(),
                    identityDocuments.get(rand.nextInt(names.size())) + UUID.randomUUID().toString());
            if (guestRepository != null) {
                guest = guestRepository.save(guest);
            }
            guests.add(guest);
        }

        // Create random clients
        List<Client> clients = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            User user = createUser();
            Client client = new Client(
                    ThreadLocalRandom.current().nextLong(999999999),
                    user,
                    names.get(rand.nextInt(names.size())) + UUID.randomUUID().toString(),
                    surnames.get(rand.nextInt(names.size())) + UUID.randomUUID().toString(),
                    identityDocuments.get(rand.nextInt(names.size())) + UUID.randomUUID().toString(),
                    "address_test_" + UUID.randomUUID().toString(),
                    "phone_number_test_" + UUID.randomUUID().toString(),
                    "stripe_id_test_" + UUID.randomUUID().toString(),
                    new Date(System.currentTimeMillis()));
            if (clientRepository != null) {
                client = clientRepository.save(client);
            }
            clients.add(client);
        }

        // Create random reservations
        List<Reservation> reservations = new ArrayList<>();
        for (int i = 0; i < 5000; i++) {
            Date date = new Date(ThreadLocalRandom.current().nextLong(
                    minDate.getTime(),
                    maxDate.getTime()));
            Reservation reservation = new Reservation(
                    ThreadLocalRandom.current().nextLong(999999999),
                    branches.get(rand.nextInt(branches.size())),
                    null,
                    null,
                    new Date(System.currentTimeMillis()),
                    date,
                    new Date(date.getTime() + 1000 * 60 * 60),
                    BigDecimal.valueOf(5.0f),
                    ReservationStatics.Status.ALL.get(rand.nextInt(ReservationStatics.Status.ALL.size())),
                    Short.valueOf("1"),
                    Short.valueOf("5"),
                    "occasion_test_" + UUID.randomUUID().toString(),
                    Boolean.TRUE);

            if (rand.nextDouble() < 0.5) {
                // Reservation with guest
                reservation.setGuest(guests.get(rand.nextInt(guests.size())));
                reservation.setByClient(Boolean.FALSE);
            } else {
                // Reservation with client
                ClientGroup owner = new ClientGroup(
                        ThreadLocalRandom.current().nextLong(999999999),
                        clients.get(rand.nextInt(clients.size())),
                        reservation,
                        true);
                if (clientGroupRepository != null) {
                    owner = clientGroupRepository.save(owner);
                }
            }

            if (reservationRepository != null) {
                reservation = reservationRepository.save(reservation);
            }
            reservations.add(reservation);
        }

        return reservations;
    }

    public ClientGuest createClientGuest(Client client) {
        if (client == null) {
            client = createClient(null);
        }

        ClientGuest clientGuest = ClientGuest.builder()
                .id(ThreadLocalRandom.current().nextLong(999999999))
                .client(client)
                .guest(null)
                .haveGuest(false)
                .build();

        if (clientGuestRepository != null) {
            clientGuest = clientGuestRepository.save(clientGuest);
        }

        return clientGuest;
    }

    public ClientGuest createClientGuest(Guest guest) {
        if (guest == null) {
            guest = createGuest();
        }

        ClientGuest clientGuest = ClientGuest.builder()
                .id(ThreadLocalRandom.current().nextLong(999999999))
                .client(null)
                .guest(guest)
                .haveGuest(true)
                .build();

        if (clientGuestRepository != null) {
            clientGuest = clientGuestRepository.save(clientGuest);
        }

        return clientGuest;
    }

    public InsiteSale createInsiteSale(Sale sale, Reservation reservation) {
        if (sale == null) {
            sale = createSale(null, null, null);
        }
        if (reservation == null) {
            reservation = createReservation(null);
        }

        InsiteSale insiteSale = InsiteSale.builder()
                .id(ThreadLocalRandom.current().nextLong(999999999))
                .sale(sale)
                .reservation(reservation)
                .build();

        if (insiteSaleRepository != null) {
            insiteSale = insiteSaleRepository.save(insiteSale);
        }

        return insiteSale;
    }

    public InsiteSaleTable createInsiteSaleTable(InsiteSale sale, Table table) {
        if (sale == null) {
            sale = createInsiteSale(null, null);
        }
        if (table == null) {
            table = createTable(null);
        }

        InsiteSaleTable insiteSaleTable = InsiteSaleTable.builder()
                .id(ThreadLocalRandom.current().nextLong(999999999))
                .insiteSale(sale)
                .table(table)
                .build();

        if (insiteSaleTableRepository != null) {
            insiteSaleTable = insiteSaleTableRepository.save(insiteSaleTable);
        }

        return insiteSaleTable;
    }

    public SaleTax createSaleTax(Sale sale, Tax tax) {
        if (sale == null) {
            sale = createSale(null, null, null);
        }
        if (tax == null) {
            tax = createTax();
        }

        SaleTax saleTax = SaleTax.builder()
                .id(ThreadLocalRandom.current().nextLong(999999999))
                .sale(sale)
                .tax(tax)
                .build();

        if (saleTaxRepository != null) {
            saleTax = saleTaxRepository.save(saleTax);
        }

        return saleTax;
    }
}
