package com.paca.paca;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import com.paca.paca.utils.TestUtils;
import com.paca.paca.sale.repository.TaxRepository;
import com.paca.paca.sale.repository.SaleRepository;
import com.paca.paca.user.repository.RoleRepository;
import com.paca.paca.user.repository.UserRepository;
import com.paca.paca.sale.repository.SaleTaxRepository;
import com.paca.paca.branch.repository.TableRepository;
import com.paca.paca.branch.repository.BranchRepository;
import com.paca.paca.business.repository.TierRepository;
import com.paca.paca.client.repository.ClientRepository;
import com.paca.paca.client.repository.FriendRepository;
import com.paca.paca.client.repository.ReviewRepository;
import com.paca.paca.branch.repository.AmenityRepository;
import com.paca.paca.sale.repository.OnlineSaleRepository;
import com.paca.paca.sale.repository.InsiteSaleRepository;
import com.paca.paca.product.repository.ProductRepository;
import com.paca.paca.sale.repository.SaleProductRepository;
import com.paca.paca.auth.repository.JwtBlackListRepository;
import com.paca.paca.branch.repository.DefaultTaxRepository;
import com.paca.paca.client.repository.ReviewLikeRepository;
import com.paca.paca.business.repository.BusinessRepository;
import com.paca.paca.reservation.repository.GuestRepository;
import com.paca.paca.client.repository.ClientGuestRepository;
import com.paca.paca.reservation.repository.InvoiceRepository;
import com.paca.paca.promotion.repository.PromotionRepository;
import com.paca.paca.branch.repository.PaymentOptionRepository;
import com.paca.paca.branch.repository.BranchAmenityRepository;
import com.paca.paca.sale.repository.InsiteSaleTableRepository;
import com.paca.paca.client.repository.FavoriteBranchRepository;
import com.paca.paca.reservation.repository.ClientGroupRepository;
import com.paca.paca.reservation.repository.ReservationRepository;
import com.paca.paca.productSubCategory.repository.ProductCategoryRepository;
import com.paca.paca.productSubCategory.repository.ProductSubCategoryRepository;

@DataJpaTest
@Testcontainers
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RepositoryTest extends PacaTest {

    @Autowired
    protected TaxRepository taxRepository;

    @Autowired
    protected TierRepository tierRepository;

    @Autowired
    protected SaleRepository saleRepository;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected RoleRepository roleRepository;

    @Autowired
    protected GuestRepository guestRepository;

    @Autowired
    protected TableRepository tableRepository;

    @Autowired
    protected ReviewRepository reviewRepository;

    @Autowired
    protected FriendRepository friendRepository;

    @Autowired
    protected BranchRepository branchRepository;

    @Autowired
    protected ClientRepository clientRepository;

    @Autowired
    protected InvoiceRepository invoiceRepository;

    @Autowired
    protected AmenityRepository amenityRepository;

    @Autowired
    protected SaleTaxRepository saleTaxRepository;

    @Autowired
    protected ProductRepository productRepository;

    @Autowired
    protected BusinessRepository businessRepository;

    @Autowired
    protected PromotionRepository promotionRepository;

    @Autowired
    protected InsiteSaleRepository insiteSaleRepository;

    @Autowired
    protected DefaultTaxRepository defaultTaxRepository;

    @Autowired
    protected ReviewLikeRepository reviewLikeRepository;

    @Autowired
    protected OnlineSaleRepository onlineSaleRepository;

    @Autowired
    protected ClientGuestRepository clientGuestRepository;

    @Autowired
    protected ReservationRepository reservationRepository;

    @Autowired
    protected SaleProductRepository saleProductRepository;

    @Autowired
    protected ClientGroupRepository clientGroupRepository;

    @Autowired
    protected JwtBlackListRepository jwtBlackListRepository;

    @Autowired
    protected BranchAmenityRepository branchAmenityRepository;

    @Autowired
    protected PaymentOptionRepository paymentOptionRepository;

    @Autowired
    protected FavoriteBranchRepository favoriteBranchRepository;

    @Autowired
    protected InsiteSaleTableRepository insiteSaleTableRepository;

    @Autowired
    protected ProductCategoryRepository productCategoryRepository;

    @Autowired
    protected ProductSubCategoryRepository productSubCategoryRepository;

    protected TestUtils utils;

    @BeforeAll
    void initUtils() {
        utils = new TestUtils(
                taxRepository,
                roleRepository,
                userRepository,
                tierRepository,
                saleRepository,
                tableRepository,
                guestRepository,
                clientRepository,
                friendRepository,
                branchRepository,
                reviewRepository,
                invoiceRepository,
                amenityRepository,
                saleTaxRepository,
                productRepository,
                businessRepository,
                promotionRepository,
                insiteSaleRepository,
                defaultTaxRepository,
                reviewLikeRepository,
                onlineSaleRepository,
                saleProductRepository,
                clientGroupRepository,
                clientGuestRepository,
                reservationRepository,
                jwtBlackListRepository,
                branchAmenityRepository,
                paymentOptionRepository,
                favoriteBranchRepository,
                insiteSaleTableRepository,
                productCategoryRepository,
                productSubCategoryRepository);
    }
}
