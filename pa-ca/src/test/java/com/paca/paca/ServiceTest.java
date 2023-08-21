package com.paca.paca;

import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.paca.paca.utils.TestUtils;

// Repositories
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

// Mapper imports
import com.paca.paca.sale.utils.TaxMapper;
import com.paca.paca.sale.utils.SaleMapper;
import com.paca.paca.branch.utils.TableMapper;
import com.paca.paca.client.utils.ClientMapper;
import com.paca.paca.client.utils.FriendMapper;
import com.paca.paca.client.utils.ReviewMapper;
import com.paca.paca.branch.utils.BranchMapper;
import com.paca.paca.branch.utils.AmenityMapper;
import com.paca.paca.product.utils.ProductMapper;
import com.paca.paca.sale.utils.SaleProductMapper;
import com.paca.paca.reservation.utils.GuestMapper;
import com.paca.paca.reservation.utils.InvoiceMapper;
import com.paca.paca.business.utils.BusinessMapper;
import com.paca.paca.promotion.utils.PromotionMapper;
import com.paca.paca.branch.utils.PaymentOptionMapper;
import com.paca.paca.reservation.utils.ReservationMapper;
import com.paca.paca.productSubCategory.utils.ProductCategoryMapper;
import com.paca.paca.productSubCategory.utils.ProductSubCategoryMapper;

@ExtendWith(MockitoExtension.class)
public class ServiceTest {

    protected TestUtils utils = TestUtils.builder().build();

    @Mock
    protected PasswordEncoder passwordEncoder;

    // Repositories
    @Mock
    protected TaxRepository taxRepository;

    @Mock
    protected TierRepository tierRepository;

    @Mock
    protected SaleRepository saleRepository;

    @Mock
    protected UserRepository userRepository;

    @Mock
    protected RoleRepository roleRepository;

    @Mock
    protected GuestRepository guestRepository;

    @Mock
    protected TableRepository tableRepository;

    @Mock
    protected ReviewRepository reviewRepository;

    @Mock
    protected FriendRepository friendRepository;

    @Mock
    protected BranchRepository branchRepository;

    @Mock
    protected ClientRepository clientRepository;

    @Mock
    protected InvoiceRepository invoiceRepository;

    @Mock
    protected AmenityRepository amenityRepository;

    @Mock
    protected SaleTaxRepository saleTaxRepository;

    @Mock
    protected ProductRepository productRepository;

    @Mock
    protected BusinessRepository businessRepository;

    @Mock
    protected PromotionRepository promotionRepository;

    @Mock
    protected InsiteSaleRepository insiteSaleRepository;

    @Mock
    protected DefaultTaxRepository defaultTaxRepository;

    @Mock
    protected ReviewLikeRepository reviewLikeRepository;

    @Mock
    protected OnlineSaleRepository onlineSaleRepository;

    @Mock
    protected ClientGuestRepository clientGuestRepository;

    @Mock
    protected ReservationRepository reservationRepository;

    @Mock
    protected SaleProductRepository saleProductRepository;

    @Mock
    protected ClientGroupRepository clientGroupRepository;

    @Mock
    protected JwtBlackListRepository jwtBlackListRepository;

    @Mock
    protected BranchAmenityRepository branchAmenityRepository;

    @Mock
    protected PaymentOptionRepository paymentOptionRepository;

    @Mock
    protected FavoriteBranchRepository favoriteBranchRepository;

    @Mock
    protected InsiteSaleTableRepository insiteSaleTableRepository;

    @Mock
    protected ProductCategoryRepository productCategoryRepository;

    @Mock
    protected ProductSubCategoryRepository productSubCategoryRepository;

    // Mappers
    @Mock
    protected TaxMapper taxMapper;

    @Mock
    protected SaleMapper saleMapper;

    @Mock
    protected GuestMapper guestMapper;

    @Mock
    protected TableMapper tableMapper;

    @Mock
    protected ReviewMapper reviewMapper;

    @Mock
    protected FriendMapper friendMapper;

    @Mock
    protected BranchMapper branchMapper;

    @Mock
    protected ClientMapper clientMapper;

    @Mock
    protected InvoiceMapper invoiceMapper;

    @Mock
    protected AmenityMapper amenityMapper;

    @Mock
    protected ProductMapper productMapper;

    @Mock
    protected BusinessMapper businessMapper;

    @Mock
    protected PromotionMapper promotionMapper;

    @Mock
    protected ReservationMapper reservationMapper;

    @Mock
    protected SaleProductMapper saleProductMapper;

    @Mock
    protected PaymentOptionMapper paymentOptionMapper;

    @Mock
    protected ProductCategoryMapper productCategoryMapper;

    @Mock
    protected ProductSubCategoryMapper productSubCategoryMapper;
}
