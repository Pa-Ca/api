package com.paca.paca.sale;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import com.paca.paca.PacaTest;
import com.paca.paca.utils.TestUtils;
import com.paca.paca.sale.model.InsiteSale;
import com.paca.paca.sale.repository.SaleRepository;
import com.paca.paca.user.repository.RoleRepository;
import com.paca.paca.user.repository.UserRepository;
import com.paca.paca.branch.repository.TableRepository;
import com.paca.paca.branch.repository.BranchRepository;
import com.paca.paca.sale.repository.InsiteSaleRepository;
import com.paca.paca.product.repository.ProductRepository;
import com.paca.paca.sale.repository.SaleProductRepository;
import com.paca.paca.business.repository.BusinessRepository;
import com.paca.paca.branch.repository.PaymentOptionRepository;
import com.paca.paca.reservation.repository.ReservationRepository;
import com.paca.paca.productSubCategory.repository.ProductCategoryRepository;
import com.paca.paca.productSubCategory.repository.ProductSubCategoryRepository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@Testcontainers
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class InsiteSaleRepositoryTest extends PacaTest {

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private TableRepository tableRepository;

    @Autowired
    private PaymentOptionRepository paymentOptionRepository;

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private SaleProductRepository saleProductRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Autowired
    private InsiteSaleRepository insiteSaleRepository;

    @Autowired
    private ProductSubCategoryRepository productSubCategoryRepository;

    private TestUtils utils;

    @BeforeAll
    void initUtils() {
        utils = TestUtils.builder()
                .roleRepository(roleRepository)
                .userRepository(userRepository)
                .reservationRepository(reservationRepository)
                .businessRepository(businessRepository)
                .branchRepository(branchRepository)
                .tableRepository(tableRepository)
                .paymentOptionRepository(paymentOptionRepository)
                .saleRepository(saleRepository)
                .productRepository(productRepository)
                .productCategoryRepository(productCategoryRepository)
                .productSubCategoryRepository(productSubCategoryRepository)
                .saleProductRepository(saleProductRepository)
                .insiteSaleRepository(insiteSaleRepository)
                .build();
    }

    @BeforeEach
    void restoreBranchDB() {
        insiteSaleRepository.deleteAll();
        saleProductRepository.deleteAll();
        saleRepository.deleteAll();
        tableRepository.deleteAll();
        reservationRepository.deleteAll();
        paymentOptionRepository.deleteAll();
        branchRepository.deleteAll();

    }

    @AfterEach
    void restoreTest() {
        insiteSaleRepository.deleteAll();
        reservationRepository.deleteAll();
        paymentOptionRepository.deleteAll();
        saleProductRepository.deleteAll();
        saleRepository.deleteAll();
        tableRepository.deleteAll();
        branchRepository.deleteAll();
        businessRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    void shouldFindBySaleId() {
        InsiteSale insiteSale = utils.createInsiteSale(null, null);

        Optional<InsiteSale> response = insiteSaleRepository.findBySaleId(insiteSale.getSale().getId());

        assertThat(response.isPresent()).isTrue();
    }

    @Test
    void shouldNotFindBySaleId() {
        InsiteSale insiteSale = utils.createInsiteSale(null, null);

        Optional<InsiteSale> response = insiteSaleRepository.findBySaleId(insiteSale.getSale().getId() + 1);

        assertThat(response.isPresent()).isFalse();
    }
}
