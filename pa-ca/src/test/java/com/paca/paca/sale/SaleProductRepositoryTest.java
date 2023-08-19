package com.paca.paca.sale;

import java.util.List;

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
import com.paca.paca.sale.model.Sale;
import com.paca.paca.utils.TestUtils;
import com.paca.paca.product.model.Product;
import com.paca.paca.sale.model.SaleProduct;
import com.paca.paca.sale.repository.SaleRepository;
import com.paca.paca.user.repository.RoleRepository;
import com.paca.paca.user.repository.UserRepository;
import com.paca.paca.branch.repository.TableRepository;
import com.paca.paca.branch.repository.BranchRepository;
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
public class SaleProductRepositoryTest extends PacaTest {

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
                .build();
    }

    @BeforeEach
    void restoreBranchDB() {
        saleProductRepository.deleteAll();
        saleRepository.deleteAll();
        tableRepository.deleteAll();
        reservationRepository.deleteAll();
        paymentOptionRepository.deleteAll();
        branchRepository.deleteAll();

    }

    @AfterEach
    void restoreTest() {
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
    void shouldGetAllBySaleId() {
        int nProducts = 10;
        Sale sale = utils.createSale(null, null, null);

        for (int i = 0; i < nProducts; i++) {
            utils.createSaleProduct(sale, null);
            utils.createSaleProduct(null, null);
        }

        List<SaleProduct> products = saleProductRepository.findAllBySaleId(sale.getId());

        assertThat(products.size()).isEqualTo(nProducts);
    }

    @Test
    void shouldDeleteAllbySaleId() {
        int nProducts = 10;
        Sale sale = utils.createSale(null, null, null);

        for (int i = 0; i < nProducts; i++) {
            utils.createSaleProduct(sale, null);
            utils.createSaleProduct(null, null);
        }

        saleProductRepository.deleteAllBySaleId(sale.getId());

        List<SaleProduct> products = saleProductRepository.findAllBySaleId(sale.getId());

        assertThat(products.size()).isEqualTo(0);
    }

    @Test
    void shoudCheckThatExistsByIdAndSale_Table_Branch_Business_Id() {
        Sale sale = utils.createSale(null, null, null);
        Product product = utils.createProduct(null);
        SaleProduct saleProduct = utils.createSaleProduct(sale, product);

        Boolean exists = saleProductRepository.existsByIdAndSale_Branch_Business_Id(
                saleProduct.getId(),
                sale.getBranch().getBusiness().getId());

        assertThat(exists).isTrue();
    }

    @Test
    void shoudCheckThatDoesNotExistsByIdAndSale_Table_Branch_Business_Id() {
        Sale sale = utils.createSale(null, null, null);
        Product product = utils.createProduct(null);
        SaleProduct saleProduct = utils.createSaleProduct(sale, product);

        Boolean exists = saleProductRepository.existsByIdAndSale_Branch_Business_Id(
                saleProduct.getId(),
                sale.getBranch().getBusiness().getId() + 1);

        assertThat(exists).isFalse();
    }

}
