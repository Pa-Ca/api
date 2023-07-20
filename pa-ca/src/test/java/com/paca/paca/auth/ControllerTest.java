package com.paca.paca.auth;

import org.springframework.boot.test.mock.mockito.MockBean;

import com.paca.paca.user.repository.UserRepository;
import com.paca.paca.branch.repository.BranchRepository;
import com.paca.paca.branch.repository.DefaultTaxRepository;
import com.paca.paca.branch.repository.PaymentOptionRepository;
import com.paca.paca.branch.repository.TableRepository;
import com.paca.paca.client.repository.ClientRepository;
import com.paca.paca.client.repository.ReviewRepository;
import com.paca.paca.product.repository.ProductRepository;
import com.paca.paca.business.repository.BusinessRepository;
import com.paca.paca.promotion.repository.PromotionRepository;
import com.paca.paca.reservation.repository.ClientGroupRepository;
import com.paca.paca.reservation.repository.ReservationRepository;
import com.paca.paca.sale.repository.SaleProductRepository;
import com.paca.paca.sale.repository.SaleRepository;
import com.paca.paca.sale.repository.TaxRepository;
import com.paca.paca.productSubCategory.repository.ProductSubCategoryRepository;

public class ControllerTest {

    @MockBean
    protected UserRepository userRepository;

    @MockBean
    protected ClientRepository clientRepository;

    @MockBean
    protected ReviewRepository reviewRepository;

    @MockBean
    protected BranchRepository branchRepository;

    @MockBean
    protected ProductRepository productRepository;

    @MockBean
    protected BusinessRepository businessRepository;

    @MockBean
    protected PromotionRepository promotionRepository;

    @MockBean
    protected ReservationRepository reservationRepository;

    @MockBean
    protected ClientGroupRepository clientGroupRepository;

    @MockBean
    protected ProductSubCategoryRepository productSubCategoryRepository;

    @MockBean
    protected SaleRepository saleRepository;

    @MockBean
    protected SaleProductRepository saleProductRepository;

    @MockBean
    protected TaxRepository taxRepository;

    @MockBean
    protected TableRepository tableRepository;

    @MockBean
    protected DefaultTaxRepository defaultTaxRepository;

    @MockBean
    protected PaymentOptionRepository paymentOptionRepository; 

}
