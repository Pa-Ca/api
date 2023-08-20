package com.paca.paca.utils;

import java.util.List;
import java.util.Date;
import java.util.UUID;
import java.util.Random;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;

import com.paca.paca.sale.model.Tax;
import com.paca.paca.sale.dto.TaxDTO;
import com.paca.paca.user.model.Role;
import com.paca.paca.user.model.User;
import com.paca.paca.sale.model.Sale;
import com.paca.paca.sale.dto.SaleDTO;
import com.paca.paca.statics.UserRole;
import com.paca.paca.auth.dto.LogoutDTO;
import com.paca.paca.branch.model.Table;
import com.paca.paca.sale.model.SaleTax;
import com.paca.paca.business.model.Tier;
import com.paca.paca.client.model.Client;
import com.paca.paca.client.model.Friend;
import com.paca.paca.client.model.Review;
import com.paca.paca.branch.model.Branch;
import com.paca.paca.branch.dto.TableDTO;
import com.paca.paca.sale.dto.SaleTaxDTO;
import com.paca.paca.statics.BusinessTier;
import com.paca.paca.branch.dto.BranchDTO;
import com.paca.paca.branch.model.Amenity;
import com.paca.paca.client.dto.ClientDTO;
import com.paca.paca.client.dto.FriendDTO;
import com.paca.paca.client.dto.ReviewDTO;
import com.paca.paca.branch.dto.AmenityDTO;
import com.paca.paca.product.model.Product;
import com.paca.paca.sale.model.InsiteSale;
import com.paca.paca.sale.model.OnlineSale;
import com.paca.paca.sale.model.SaleProduct;
import com.paca.paca.product.dto.ProductDTO;
import com.paca.paca.sale.dto.SaleProductDTO;
import com.paca.paca.reservation.model.Guest;
import com.paca.paca.client.model.ReviewLike;
import com.paca.paca.business.model.Business;
import com.paca.paca.sale.statics.TaxStatics;
import com.paca.paca.branch.model.DefaultTax;
import com.paca.paca.sale.statics.SaleStatics;
import com.paca.paca.branch.dto.DefaultTaxDTO;
import com.paca.paca.business.dto.BusinessDTO;
import com.paca.paca.auth.dto.LoginRequestDTO;
import com.paca.paca.client.model.ClientGuest;
import com.paca.paca.reservation.dto.GuestDTO;
import com.paca.paca.reservation.model.Invoice;
import com.paca.paca.promotion.model.Promotion;
import com.paca.paca.auth.dto.LoginResponseDTO;
import com.paca.paca.auth.dto.SignupRequestDTO;
import com.paca.paca.reservation.dto.InvoiceDTO;
import com.paca.paca.sale.model.InsiteSaleTable;
import com.paca.paca.auth.dto.RefreshRequestDTO;
import com.paca.paca.promotion.dto.PromotionDTO;
import com.paca.paca.branch.model.BranchAmenity;
import com.paca.paca.branch.model.PaymentOption;
import com.paca.paca.branch.dto.PaymentOptionDTO;
import com.paca.paca.auth.dto.RefreshResponseDTO;
import com.paca.paca.client.model.FavoriteBranch;
import com.paca.paca.reservation.model.ClientGroup;
import com.paca.paca.reservation.model.Reservation;
import com.paca.paca.sale.repository.TaxRepository;
import com.paca.paca.reservation.dto.ReservationDTO;
import com.paca.paca.user.repository.RoleRepository;
import com.paca.paca.user.repository.UserRepository;
import com.paca.paca.sale.repository.SaleRepository;
import com.paca.paca.sale.repository.SaleTaxRepository;
import com.paca.paca.branch.repository.TableRepository;
import com.paca.paca.business.repository.TierRepository;
import com.paca.paca.client.repository.ClientRepository;
import com.paca.paca.client.repository.FriendRepository;
import com.paca.paca.client.repository.ReviewRepository;
import com.paca.paca.branch.repository.BranchRepository;
import com.paca.paca.branch.repository.AmenityRepository;
import com.paca.paca.product.repository.ProductRepository;
import com.paca.paca.sale.repository.InsiteSaleRepository;
import com.paca.paca.sale.repository.SaleProductRepository;
import com.paca.paca.auth.repository.JwtBlackListRepository;
import com.paca.paca.branch.repository.DefaultTaxRepository;
import com.paca.paca.reservation.repository.GuestRepository;
import com.paca.paca.client.repository.ReviewLikeRepository;
import com.paca.paca.business.repository.BusinessRepository;
import com.paca.paca.reservation.statics.ReservationStatics;
import com.paca.paca.client.repository.ClientGuestRepository;
import com.paca.paca.reservation.repository.InvoiceRepository;
import com.paca.paca.productSubCategory.model.ProductCategory;
import com.paca.paca.promotion.repository.PromotionRepository;
import com.paca.paca.sale.repository.InsiteSaleTableRepository;
import com.paca.paca.sale.repository.OnlineSaleRepository;
import com.paca.paca.branch.repository.BranchAmenityRepository;
import com.paca.paca.branch.repository.PaymentOptionRepository;
import com.paca.paca.productSubCategory.dto.ProductCategoryDTO;
import com.paca.paca.client.repository.FavoriteBranchRepository;
import com.paca.paca.productSubCategory.model.ProductSubCategory;
import com.paca.paca.productSubCategory.dto.ProductSubCategoryDTO;
import com.paca.paca.reservation.repository.ClientGroupRepository;
import com.paca.paca.reservation.repository.ReservationRepository;
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

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class TestUtils {

    TaxRepository taxRepository;

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

    SaleTaxRepository saleTaxRepository;

    ProductRepository productRepository;

    BusinessRepository businessRepository;

    PromotionRepository promotionRepository;

    InsiteSaleRepository insiteSaleRepository;

    DefaultTaxRepository defaultTaxRepository;

    ReviewLikeRepository reviewLikeRepository;

    OnlineSaleRepository onlineSaleRepository;

    SaleProductRepository saleProductRepository;

    ClientGroupRepository clientGroupRepository;

    ClientGuestRepository clientGuestRepository;

    ReservationRepository reservationRepository;

    JwtBlackListRepository jwtBlackListRepository;

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
        return new SignupRequestDTO(
                UUID.randomUUID().toString() + "@test.com",
                UUID.randomUUID().toString(),
                UserRole.client.name());
    }

    public LoginRequestDTO createLoginRequestDTO() {
        return new LoginRequestDTO(
                UUID.randomUUID().toString() + "@test.com",
                UUID.randomUUID().toString());
    }

    public RefreshRequestDTO createRefreshRequestDTO() {
        return new RefreshRequestDTO(
                UUID.randomUUID().toString());
    }

    public LoginResponseDTO createLoginResponseDTO() {
        return new LoginResponseDTO(
                ThreadLocalRandom.current().nextLong(999999999),
                UserRole.client.name(),
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString());
    }

    public RefreshResponseDTO createRefreshResponseDTO() {
        return new RefreshResponseDTO(
                UUID.randomUUID().toString());
    }

    public LogoutDTO createLogoutDTO() {
        return new LogoutDTO(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString());
    }

    public User createUser() {
        Role role = new Role(
                (long) UserRole.client.ordinal(),
                UserRole.client);
        User user = new User(
                ThreadLocalRandom.current().nextLong(999999999),
                role,
                UUID.randomUUID().toString() + "@test.com",
                UUID.randomUUID().toString(),
                false,
                "provider_" + UUID.randomUUID().toString(),
                "provider_id_" + UUID.randomUUID().toString());

        if (roleRepository != null) {
            roleRepository.save(user.getRole());
        }
        if (userRepository != null) {
            user = userRepository.save(user);
        }

        return user;
    }

    public Client createClient(User user) {
        if (user == null) {
            user = createUser();
        }

        Client client = new Client(
                ThreadLocalRandom.current().nextLong(999999999),
                user,
                "name_" + UUID.randomUUID().toString().substring(0, 25),
                "surname_" + UUID.randomUUID().toString().substring(0, 25),
                "V" + ThreadLocalRandom.current().nextLong(999999999),
                "address_" + UUID.randomUUID().toString(),
                "+58" + ThreadLocalRandom.current().nextLong(9999999),
                "stripe_customer_id_" + UUID.randomUUID().toString(),
                new java.util.Date(System.currentTimeMillis()));

        if (clientRepository != null) {
            client = clientRepository.save(client);
        }

        return client;
    }

    public ClientDTO createClientDTO(Client client) {
        if (client == null) {
            client = createClient(null);
        }

        ClientDTO dto = new ClientDTO(
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

        Friend request = new Friend(
                ThreadLocalRandom.current().nextLong(999999999),
                requester,
                addresser,
                accepted,
                rejected);

        if (friendRepository != null) {
            request = friendRepository.save(request);
        }

        return request;
    }

    public FriendDTO createFriendRequestDTO(Friend request) {
        if (request == null) {
            request = createFriendRequest(null, null, false, false);
        }

        FriendDTO dto = new FriendDTO(
                request.getId(),
                request.getRequester().getId(),
                request.getAddresser().getId(),
                request.getAccepted(),
                request.getRejected());

        return dto;
    }

    public Tier createTier(BusinessTier businessTier) {
        Tier tier = new Tier(
                (long) businessTier.ordinal(),
                businessTier,
                1,
                BigDecimal.valueOf(1f));

        return tier;
    }

    public Tier createTier(BusinessTier businessTier, int reservationLimit, BigDecimal tierCost) {
        Tier tier = new Tier(
                (long) businessTier.ordinal(),
                businessTier,
                reservationLimit,
                tierCost);

        return tier;
    }

    public Business createBusiness(User user) {
        if (user == null) {
            user = createUser();
        }

        Business business = new Business(
                ThreadLocalRandom.current().nextLong(999999999),
                user,
                createTier(BusinessTier.basic),
                "name_" + UUID.randomUUID().toString(),
                false,
                "+58" + ThreadLocalRandom.current().nextLong(9999999));

        if (businessRepository != null) {
            business = businessRepository.save(business);
        }
        return business;
    }

    public Business createBusiness(User user, Tier tier) {
        if (user == null) {
            user = createUser();
        }

        Business business = new Business(
                ThreadLocalRandom.current().nextLong(999999999),
                user,
                tier,
                "name_" + UUID.randomUUID().toString(),
                false,
                "+58" + ThreadLocalRandom.current().nextLong(9999999));

        if (businessRepository != null) {
            business = businessRepository.save(business);
        }
        return business;
    }

    public BusinessDTO createBusinessDTO(Business business) {
        if (business == null) {
            business = createBusiness(null);
        }

        BusinessDTO dto = new BusinessDTO(
                business.getId(),
                business.getUser().getId(),
                business.getUser().getEmail(),
                business.getTier().getName().name(),
                business.getName(),
                business.getVerified(),
                business.getPhoneNumber());

        return dto;
    }

    public Branch createBranch(Business business) {
        if (business == null) {
            business = createBusiness(null);
        }

        Branch branch = new Branch(
                ThreadLocalRandom.current().nextLong(999999999),
                business,
                "name_" + UUID.randomUUID().toString(),
                ThreadLocalRandom.current().nextFloat() * 5,
                (short) ThreadLocalRandom.current().nextInt(1, 1000 + 1),
                new BigDecimal(Math.random()),
                "maps_link_" + UUID.randomUUID().toString(),
                "location_" + UUID.randomUUID().toString(),
                "overview_" + UUID.randomUUID().toString(),
                true,
                false,
                "+58" + ThreadLocalRandom.current().nextLong(9999999),
                "type_" + UUID.randomUUID().toString(),
                LocalTime.of(
                        ThreadLocalRandom.current().nextInt(0, 23 + 1),
                        ThreadLocalRandom.current().nextInt(0, 59 + 1),
                        ThreadLocalRandom.current().nextInt(0, 59 + 1)),
                LocalTime.of(
                        ThreadLocalRandom.current().nextInt(0, 23 + 1),
                        ThreadLocalRandom.current().nextInt(0, 59 + 1),
                        ThreadLocalRandom.current().nextInt(0, 59 + 1)),
                Duration.ofHours(ThreadLocalRandom.current().nextInt(0, 23 + 1))
                        .plusMinutes(ThreadLocalRandom.current().nextInt(0, 59 + 1))
                        .plusSeconds(ThreadLocalRandom.current().nextInt(0, 59 + 1)),
                ThreadLocalRandom.current().nextFloat() * 100,
                false);

        if (branchRepository != null) {
            branch = branchRepository.save(branch);
        }

        return branch;
    }

    public BranchDTO createBranchDTO(Branch branch) {
        if (branch == null) {
            branch = createBranch(null);
        }

        BranchDTO dto = new BranchDTO(
                branch.getId(),
                branch.getBusiness().getId(),
                branch.getName(),
                branch.getScore(),
                branch.getCapacity(),
                branch.getReservationPrice(),
                branch.getMapsLink(),
                branch.getLocation(),
                branch.getOverview(),
                branch.getVisibility(),
                branch.getReserveOff(),
                branch.getPhoneNumber(),
                branch.getType(),
                branch.getHourIn(),
                branch.getHourOut(),
                branch.getAverageReserveTime(),
                branch.getDollarExchange(),
                branch.getDeleted());

        return dto;
    }

    public FavoriteBranch createFavoriteBranch(Client client, Branch branch) {
        if (client == null) {
            client = createClient(null);
        }
        if (branch == null) {
            branch = createBranch(null);
        }

        FavoriteBranch favoriteBranch = new FavoriteBranch(
                ThreadLocalRandom.current().nextLong(999999999),
                client,
                branch);

        if (favoriteBranchRepository != null) {
            favoriteBranch = favoriteBranchRepository.save(favoriteBranch);
        }

        return favoriteBranch;
    }

    public Amenity createAmenity() {
        Amenity amenity = new Amenity(
                ThreadLocalRandom.current().nextLong(999999999),
                "name_" + UUID.randomUUID().toString());

        if (amenityRepository != null) {
            amenity = amenityRepository.save(amenity);
        }

        return amenity;
    }

    public AmenityDTO createAmenityDTO(Amenity amenity) {
        if (amenity == null) {
            amenity = createAmenity();
        }

        AmenityDTO dto = new AmenityDTO(
                amenity.getId(),
                amenity.getName());

        return dto;
    }

    public BranchAmenity createBranchAmenity(Branch branch, Amenity amenity) {
        if (branch == null) {
            branch = createBranch(null);
        }
        if (amenity == null) {
            amenity = createAmenity();
        }

        BranchAmenity branchAmenity = new BranchAmenity(
                ThreadLocalRandom.current().nextLong(999999999),
                branch,
                amenity);

        if (branchAmenityRepository != null) {
            branchAmenity = branchAmenityRepository.save(branchAmenity);
        }

        return branchAmenity;
    }

    public Product createProduct(ProductSubCategory subCategory) {
        if (subCategory == null) {
            subCategory = createProductSubCategory(null, null);
        }

        Product product = new Product(
                ThreadLocalRandom.current().nextLong(999999999),
                subCategory,
                "name_" + UUID.randomUUID().toString(),
                BigDecimal.valueOf(ThreadLocalRandom.current().nextFloat() * 100),
                "description_" + UUID.randomUUID().toString(),
                false);

        if (productRepository != null) {
            product = productRepository.save(product);
        }

        return product;
    }

    public ProductDTO createProductDTO(Product product) {
        if (product == null) {
            product = createProduct(null);
        }

        ProductDTO dto = new ProductDTO(
                product.getId(),
                product.getSubCategory().getId(),
                product.getName(),
                product.getPrice(),
                product.getDescription(),
                product.getDisabled());

        return dto;
    }

    public ProductCategory createProductCategory() {
        ProductCategory category = new ProductCategory(
                ThreadLocalRandom.current().nextLong(999999999),
                "name_" + UUID.randomUUID().toString());

        if (productCategoryRepository != null) {
            category = productCategoryRepository.save(category);
        }

        return category;
    }

    public ProductCategoryDTO createProductCategoryDTO(ProductCategory category) {
        if (category == null) {
            category = createProductCategory();
        }

        ProductCategoryDTO dto = new ProductCategoryDTO(
                category.getId(),
                category.getName());

        return dto;

    }

    public ProductSubCategory createProductSubCategory(Branch branch, ProductCategory category) {
        if (branch == null) {
            branch = createBranch(null);
        }
        if (category == null) {
            category = createProductCategory();
        }

        ProductSubCategory subCategory = new ProductSubCategory(
                ThreadLocalRandom.current().nextLong(999999999),
                branch,
                category,
                "name_" + UUID.randomUUID().toString());

        if (productSubCategoryRepository != null) {
            subCategory = productSubCategoryRepository.save(subCategory);
        }

        return subCategory;
    }

    public ProductSubCategoryDTO createProductSubCategoryDTO(ProductSubCategory subCategory) {
        if (subCategory == null) {
            subCategory = createProductSubCategory(null, null);
        }

        ProductSubCategoryDTO dto = new ProductSubCategoryDTO(
                subCategory.getId(),
                subCategory.getBranch().getId(),
                subCategory.getCategory().getId(),
                subCategory.getName());

        return dto;
    }

    public Review createReview(Client client, Branch branch) {
        if (client == null) {
            client = createClient(null);
        }
        if (branch == null) {
            branch = createBranch(null);
        }

        Review review = new Review(
                ThreadLocalRandom.current().nextLong(999999999),
                client,
                branch,
                "text_" + UUID.randomUUID().toString(),
                new Date(System.currentTimeMillis()));

        if (reviewRepository != null) {
            review = reviewRepository.save(review);
        }

        return review;
    }

    public ReviewDTO createReviewDTO(Review review) {
        if (review == null) {
            review = createReview(null, null);
        }

        ReviewDTO dto = new ReviewDTO(
                review.getId(),
                review.getClient().getId(),
                review.getBranch().getId(),
                review.getText(),
                review.getDate(),
                ThreadLocalRandom.current().nextInt(0, 1001));

        return dto;
    }

    public ReviewLike createReviewLike(Client client, Review review) {
        if (client == null) {
            client = createClient(null);
        }
        if (review == null) {
            review = createReview(null, null);
        }

        ReviewLike like = new ReviewLike(
                ThreadLocalRandom.current().nextLong(999999999),
                review,
                client);

        if (reviewLikeRepository != null) {
            like = reviewLikeRepository.save(like);
        }

        return like;
    }

    public Guest createGuest() {
        Guest guest = new Guest(
                ThreadLocalRandom.current().nextLong(999999999),
                "name_" + UUID.randomUUID().toString().substring(0, 25),
                "surname_" + UUID.randomUUID().toString().substring(0, 25),
                "email_" + UUID.randomUUID().toString(),
                "+58" + ThreadLocalRandom.current().nextLong(9999999),
                "V" + ThreadLocalRandom.current().nextLong(999999999));

        if (guestRepository != null) {
            guest = guestRepository.save(guest);
        }

        return guest;
    }

    public GuestDTO createGuestDTO(Guest guest) {
        if (guest == null) {
            guest = createGuest();
        }

        GuestDTO dto = new GuestDTO(
                guest.getId(),
                guest.getName(),
                guest.getSurname(),
                guest.getEmail(),
                guest.getPhoneNumber(),
                guest.getIdentityDocument());

        return dto;
    }

    public Reservation createReservation(Branch branch) {
        if (branch == null) {
            branch = createBranch(null);
        }

        Reservation reservation = new Reservation(
                ThreadLocalRandom.current().nextLong(999999999),
                branch,
                null,
                null,
                new Date(System.currentTimeMillis()),
                new Date(System.currentTimeMillis()),
                new Date(System.currentTimeMillis() + 1000 * 60 * 60),
                new BigDecimal(Math.random()),
                ReservationStatics.Status.PENDING,
                (short) ThreadLocalRandom.current().nextInt(0, 1001),
                (short) ThreadLocalRandom.current().nextInt(0, branch.getCapacity() + 1),
                "occasion_" + UUID.randomUUID().toString(),
                true);

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

        Reservation reservation = new Reservation(
                ThreadLocalRandom.current().nextLong(999999999),
                branch,
                guest,
                null,
                new Date(System.currentTimeMillis()),
                new Date(System.currentTimeMillis()),
                new Date(System.currentTimeMillis() + 1000 * 60 * 60),
                new BigDecimal(Math.random()),
                ReservationStatics.Status.PENDING,
                (short) ThreadLocalRandom.current().nextInt(0, 1001),
                (short) ThreadLocalRandom.current().nextInt(0, 1001),
                "occasion_" + UUID.randomUUID().toString(),
                false);

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
            invoice = createInvoice();
        }

        Reservation reservation = new Reservation(
                ThreadLocalRandom.current().nextLong(999999999),
                branch,
                guest,
                invoice,
                new Date(System.currentTimeMillis()),
                new Date(System.currentTimeMillis()),
                new Date(System.currentTimeMillis() + 1000 * 60 * 60),
                new BigDecimal(Math.random()),
                ReservationStatics.Status.PENDING,
                (short) ThreadLocalRandom.current().nextInt(0, 1001),
                (short) ThreadLocalRandom.current().nextInt(0, 1001),
                "occasion_" + UUID.randomUUID().toString(),
                false);

        if (reservationRepository != null) {
            reservation = reservationRepository.save(reservation);
        }

        return reservation;
    }

    public ReservationDTO createReservationDTO(Reservation reservation) {
        if (reservation == null) {
            reservation = createReservation(null);
        }

        ReservationDTO dto = new ReservationDTO(
                reservation.getId(),
                reservation.getBranch().getId(),
                reservation.getGuest() != null ? reservation.getGuest().getId() : null,
                reservation.getInvoice() != null ? reservation.getInvoice().getId() : null,
                reservation.getRequestDate(),
                reservation.getReservationDateIn(),
                reservation.getReservationDateOut(),
                reservation.getPrice(),
                reservation.getStatus(),
                reservation.getTableNumber(),
                reservation.getClientNumber(),
                reservation.getOccasion(),
                reservation.getByClient());

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
                    names.get(rand.nextInt(names.size())) + UUID.randomUUID().toString().substring(0, 25),
                    surnames.get(rand.nextInt(names.size())) + UUID.randomUUID().toString().substring(0, 25),
                    "email_test_" + UUID.randomUUID().toString(),
                    "+58" + ThreadLocalRandom.current().nextLong(9999999),
                    identityDocuments.get(rand.nextInt(identityDocuments.size()))
                            + UUID.randomUUID().toString().substring(0, 10));
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
                    names.get(rand.nextInt(names.size())) + UUID.randomUUID().toString().substring(0, 25),
                    surnames.get(rand.nextInt(names.size())) + UUID.randomUUID().toString().substring(0, 25),
                    identityDocuments.get(rand.nextInt(identityDocuments.size()))
                            + UUID.randomUUID().toString().substring(0, 10),
                    "address_test_" + UUID.randomUUID().toString(),
                    "+58" + ThreadLocalRandom.current().nextLong(9999999),
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

                if (reservationRepository != null) {
                    reservation = reservationRepository.save(reservation);
                }
            } else {
                if (reservationRepository != null) {
                    reservation = reservationRepository.save(reservation);
                }

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

            reservations.add(reservation);
        }

        return reservations;
    }

    public Promotion createPromotion(Branch branch) {
        if (branch == null) {
            branch = createBranch(null);
        }

        Promotion promotion = new Promotion(
                ThreadLocalRandom.current().nextLong(999999999),
                branch,
                "text_" + UUID.randomUUID().toString(),
                false);

        if (promotionRepository != null) {
            promotion = promotionRepository.save(promotion);
        }

        return promotion;
    }

    public PromotionDTO createPromotionDTO(Promotion promotion) {
        if (promotion == null) {
            promotion = createPromotion(null);
        }

        PromotionDTO dto = new PromotionDTO(
                promotion.getId(),
                promotion.getBranch().getId(),
                promotion.getText(),
                promotion.getDisabled());

        return dto;
    }

    public ClientGroup createClientGroup(Client client, Reservation reservation) {
        if (client == null) {
            client = createClient(null);
        }
        if (reservation == null) {
            reservation = createReservation(null);
        }

        ClientGroup clientGroup = new ClientGroup(
                ThreadLocalRandom.current().nextLong(999999999),
                client,
                reservation,
                true);

        if (clientGroupRepository != null) {
            clientGroup = clientGroupRepository.save(clientGroup);
        }
        return clientGroup;
    }

    public ClientGroup createClientGroup(Client client, Reservation reservation, Boolean owner) {
        if (client == null) {
            client = createClient(null);
        }
        if (reservation == null) {
            reservation = createReservation(null);
        }

        ClientGroup clientGroup = new ClientGroup(
                ThreadLocalRandom.current().nextLong(999999999),
                client,
                reservation,
                owner);

        if (clientGroupRepository != null) {
            clientGroup = clientGroupRepository.save(clientGroup);
        }
        return clientGroup;
    }

    public Tax createTax() {
        Tax tax = new Tax(
                ThreadLocalRandom.current().nextLong(999999999),
                TaxStatics.Types.PERCENTAGE,
                "name_" + UUID.randomUUID().toString(),
                ThreadLocalRandom.current().nextFloat() * 100);

        if (taxRepository != null) {
            tax = taxRepository.save(tax);
        }

        return tax;
    }

    public TaxDTO createTaxDTO(Tax tax) {
        if (tax == null) {
            tax = createTax();
        }

        TaxDTO dto = new TaxDTO(
                tax.getId(),
                tax.getType(),
                tax.getName(),
                tax.getValue());

        return dto;
    }

    public DefaultTax createDefaultTax(Tax tax, Branch branch) {
        if (tax == null) {
            tax = createTax();
        }
        if (branch == null) {
            branch = createBranch(null);
        }

        DefaultTax defaultTax = new DefaultTax(
                ThreadLocalRandom.current().nextLong(999999999),
                branch,
                tax);

        if (defaultTaxRepository != null) {
            defaultTax = defaultTaxRepository.save(defaultTax);
        }

        return defaultTax;
    }

    public DefaultTaxDTO createDefaultTaxDTO(DefaultTax defaultTax) {
        if (defaultTax == null) {
            defaultTax = createDefaultTax(null, null);
        }

        DefaultTaxDTO dto = new DefaultTaxDTO(
                defaultTax.getId(),
                defaultTax.getBranch().getId(),
                createTaxDTO(defaultTax.getTax()));

        return dto;
    }

    public Table createTable(Branch branch) {
        if (branch == null) {
            branch = createBranch(null);
        }

        Table table = new Table(
                ThreadLocalRandom.current().nextLong(999999999),
                branch,
                "Table_" + UUID.randomUUID().toString());

        if (tableRepository != null) {
            table = tableRepository.save(table);
        }

        return table;
    }

    public TableDTO createTableDTO(Table table) {
        if (table == null) {
            table = createTable(null);
        }

        TableDTO dto = new TableDTO(
                table.getId(),
                table.getBranch().getId(),
                table.getName());

        return dto;
    }

    public PaymentOption createPaymentOption(Branch branch) {
        if (branch == null) {
            branch = createBranch(null);
        }

        PaymentOption paymentOption = new PaymentOption(
                ThreadLocalRandom.current().nextLong(999999999),
                branch,
                "name_" + UUID.randomUUID().toString(),
                "description_" + UUID.randomUUID().toString());

        if (paymentOptionRepository != null) {
            paymentOption = paymentOptionRepository.save(paymentOption);
        }

        return paymentOption;
    }

    public PaymentOptionDTO createPaymentOptionDTO(PaymentOption paymentOption) {
        if (paymentOption == null) {
            paymentOption = createPaymentOption(null);
        }

        PaymentOptionDTO dto = new PaymentOptionDTO(
                paymentOption.getId(),
                paymentOption.getBranch().getId(),
                paymentOption.getName(),
                paymentOption.getDescription());

        return dto;
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

        Sale sale = new Sale(
                ThreadLocalRandom.current().nextLong(999999999),
                branch,
                clientGuest,
                invoice,
                (short) ThreadLocalRandom.current().nextInt(0, 1001),
                SaleStatics.Status.ONGOING,
                new Date(System.currentTimeMillis()),
                new Date(System.currentTimeMillis() + 1000 * 60 * 60),
                ThreadLocalRandom.current().nextFloat() * 100,
                "note_" + UUID.randomUUID().toString());

        if (saleRepository != null) {
            sale = saleRepository.save(sale);
        }

        return sale;
    }

    public SaleDTO createSaleDTO(Sale sale) {
        if (sale == null) {
            sale = createSale(null, null, null);
        }

        SaleDTO dto = new SaleDTO(
                sale.getId(),
                sale.getBranch().getId(),
                sale.getClientGuest() != null ? sale.getClientGuest().getId() : null,
                sale.getInvoice() != null ? sale.getInvoice().getId() : null,
                sale.getClientQuantity(),
                sale.getStatus(),
                sale.getStartTime(),
                sale.getEndTime(),
                sale.getDollarExchange(),
                sale.getNote());

        return dto;
    }

    public SaleProduct createSaleProduct(Sale sale, Product product) {
        if (sale == null) {
            sale = createSale(null, null, null);
        }
        if (product == null) {
            product = createProduct(null);
        }

        SaleProduct saleProduct = new SaleProduct(
                ThreadLocalRandom.current().nextLong(999999999),
                sale,
                product,
                "name_" + UUID.randomUUID().toString(),
                ThreadLocalRandom.current().nextInt(0, 1001),
                BigDecimal.valueOf(ThreadLocalRandom.current().nextFloat() * 100));

        if (saleProductRepository != null) {
            saleProduct = saleProductRepository.save(saleProduct);
        }

        return saleProduct;
    }

    public SaleProductDTO createSaleProductDTO(SaleProduct saleProduct) {
        if (saleProduct == null) {
            saleProduct = createSaleProduct(null, null);
        }

        SaleProductDTO dto = new SaleProductDTO(
                saleProduct.getId(),
                saleProduct.getSale().getId(),
                saleProduct.getProduct().getId(),
                saleProduct.getName(),
                saleProduct.getAmount(),
                saleProduct.getPrice());

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
                    names.get(rand.nextInt(names.size())) + UUID.randomUUID().toString().substring(0, 25),
                    surnames.get(rand.nextInt(names.size())) + UUID.randomUUID().toString().substring(0, 25),
                    "email_test_" + UUID.randomUUID().toString(),
                    "+58" + ThreadLocalRandom.current().nextLong(9999999),
                    identityDocuments.get(rand.nextInt(identityDocuments.size()))
                            + UUID.randomUUID().toString().substring(0, 10));
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
                    names.get(rand.nextInt(names.size())) + UUID.randomUUID().toString().substring(0, 25),
                    surnames.get(rand.nextInt(names.size())) + UUID.randomUUID().toString().substring(0, 25),
                    identityDocuments.get(rand.nextInt(identityDocuments.size()))
                            + UUID.randomUUID().toString().substring(0, 10),
                    "address_test_" + UUID.randomUUID().toString(),
                    "+58" + ThreadLocalRandom.current().nextLong(9999999),
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

            if (clientGuestRepository != null) {
                clientGuest = clientGuestRepository.save(clientGuest);
            }
            sale.setClientGuest(clientGuest);
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
            invoice = createInvoice();
        }

        InvoiceDTO dto = new InvoiceDTO(
                invoice.getId(),
                invoice.getPayDate(),
                invoice.getPrice(),
                invoice.getPayment(),
                invoice.getPaymentCode());

        return dto;
    }

    public ClientGuest createClientGuest(Client client) {
        if (client == null) {
            client = createClient(null);
        }

        ClientGuest clientGuest = new ClientGuest(
                ThreadLocalRandom.current().nextLong(999999999),
                client,
                null,
                false);

        if (clientGuestRepository != null) {
            clientGuest = clientGuestRepository.save(clientGuest);
        }

        return clientGuest;
    }

    public ClientGuest createClientGuest(Guest guest) {
        if (guest == null) {
            guest = createGuest();
        }

        ClientGuest clientGuest = new ClientGuest(
                ThreadLocalRandom.current().nextLong(999999999),
                null,
                guest,
                true);

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

        InsiteSale insiteSale = new InsiteSale(
                ThreadLocalRandom.current().nextLong(999999999),
                sale,
                reservation);

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

        InsiteSaleTable insiteSaleTable = new InsiteSaleTable(
                ThreadLocalRandom.current().nextLong(999999999),
                sale,
                table);

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

        SaleTax saleTax = new SaleTax(
                ThreadLocalRandom.current().nextLong(999999999),
                sale,
                tax);

        if (saleTaxRepository != null) {
            saleTax = saleTaxRepository.save(saleTax);
        }

        return saleTax;
    }

    public SaleTaxDTO createSaleTaxDTO(SaleTax saleTax) {
        if (saleTax == null) {
            saleTax = createSaleTax(null, null);
        }

        SaleTaxDTO dto = new SaleTaxDTO(
                saleTax.getId(),
                saleTax.getSale().getId(),
                createTaxDTO(saleTax.getTax()));

        return dto;
    }

    public OnlineSale createOnlineSale(Sale sale) {
        if (sale == null) {
            sale = createSale(null, null, null);
        }

        OnlineSale onlineSale = new OnlineSale(
                ThreadLocalRandom.current().nextLong(999999999),
                sale);

        if (onlineSaleRepository != null) {
            onlineSale = onlineSaleRepository.save(onlineSale);
        }

        return onlineSale;
    }
}
