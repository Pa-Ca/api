package com.paca.paca.sale;

import java.math.BigDecimal;
import java.util.ArrayList;
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
import com.paca.paca.branch.repository.BranchRepository;
import com.paca.paca.branch.repository.TableRepository;
import com.paca.paca.business.repository.BusinessRepository;
import com.paca.paca.product.model.Product;
import com.paca.paca.product.repository.ProductRepository;
import com.paca.paca.productSubCategory.repository.ProductCategoryRepository;
import com.paca.paca.productSubCategory.repository.ProductSubCategoryRepository;
import com.paca.paca.reservation.repository.ReservationRepository;
import com.paca.paca.sale.model.Sale;
import com.paca.paca.sale.model.SaleProduct;
import com.paca.paca.sale.repository.SaleProductRepository;
import com.paca.paca.sale.repository.SaleRepository;
import com.paca.paca.user.repository.RoleRepository;
import com.paca.paca.user.repository.UserRepository;
import com.paca.paca.utils.TestUtils;

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
        branchRepository.deleteAll();

    }

    @AfterEach
    void restoreTest() {
        reservationRepository.deleteAll();
        saleProductRepository.deleteAll();
        saleRepository.deleteAll();
        tableRepository.deleteAll();
        branchRepository.deleteAll();
        businessRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    void shouldCreateSaleProduct() {
        // Create a sale using the utils
        Sale sale = utils.createSale(null, null);
        // Create a product using the utils
        Product product = utils.createProduct(null);

        // Create a sale product using the builder
        SaleProduct saleProduct = SaleProduct.builder()
                .sale(sale)
                .product(product)
                .amount(42069)
                .name(product.getName())
                .price(BigDecimal.valueOf(1000))
                .build();

        // Save the sale product
        SaleProduct savedSaleProduct = saleProductRepository.save(saleProduct);

        // Assert that the sale product was saved
        assert saleProductRepository.findAll().size() == 1;

        // Assert that the sale product was saved correctly
        assertThat(savedSaleProduct).isNotNull();

        // Assert that the sale product has the correct sale
        assertThat(savedSaleProduct.getSale()).isEqualTo(sale);
        // Assert that the sale product has the correct product, price, name and amount
        assertThat(savedSaleProduct.getProduct()).isEqualTo(product);
        assertThat(savedSaleProduct.getPrice()).isEqualTo(saleProduct.getPrice());
        assertThat(savedSaleProduct.getName()).isEqualTo(saleProduct.getName());
        assertThat(savedSaleProduct.getAmount()).isEqualTo(saleProduct.getAmount());

    }

    @Test
    void shouldGetAllSaleProducts() {
        // Create a list of 7 sale products using the utils

        List<SaleProduct> saleProducts = new ArrayList<>();
        // Create a 7 sales using the utils
        for (int i = 0; i < 7; i++) {
            saleProducts.add(utils.createSaleProduct(null, null));
            // Save the sale product
            saleProductRepository.save(saleProducts.get(i));
        }

        List<SaleProduct> savedSaleProducts = saleProductRepository.findAll();

        // Assert that the sale product was saved
        assert saleProductRepository.findAll().size() == 7;

        // Assert that the sale product was saved correctly
        assertThat(savedSaleProducts).isNotNull();

        assertThat(savedSaleProducts).isEqualTo(saleProducts);

    }

    @Test
    void shouldFindAllBySaleId() {
        // Create two sales using the utils
        Sale sale1 = utils.createSale(null, null);
        Sale sale2 = utils.createSale(null, null);

        // Create a list of 7 sale products using the utils for each sale
        List<SaleProduct> saleProducts1 = new ArrayList<>();
        List<SaleProduct> saleProducts2 = new ArrayList<>();

        // Create a 7 sales using the utils
        for (int i = 0; i < 7; i++) {
            saleProducts1.add(utils.createSaleProduct(sale1, null));
            saleProducts2.add(utils.createSaleProduct(sale2, null));
            // Save the sale product
            saleProductRepository.save(saleProducts1.get(i));
            saleProductRepository.save(saleProducts2.get(i));
        }

        List<SaleProduct> savedSaleProducts1 = saleProductRepository.findAllBySaleId(sale1.getId());
        List<SaleProduct> savedSaleProducts2 = saleProductRepository.findAllBySaleId(sale2.getId());

        // Now compare the two lists
        assertThat(savedSaleProducts1).isEqualTo(saleProducts1);
        assertThat(savedSaleProducts2).isEqualTo(saleProducts2);
    }

    @Test
    void shouldDeleteAllbySaleId() {
        // Create two sales using the utils
        Sale sale1 = utils.createSale(null, null);
        Sale sale2 = utils.createSale(null, null);

        // Create a list of 7 sale products using the utils for each sale
        List<SaleProduct> saleProducts1 = new ArrayList<>();
        List<SaleProduct> saleProducts2 = new ArrayList<>();

        // Create a 7 sales using the utils
        for (int i = 0; i < 7; i++) {
            saleProducts1.add(utils.createSaleProduct(sale1, null));
            saleProducts2.add(utils.createSaleProduct(sale2, null));
            // Save the sale product
            saleProductRepository.save(saleProducts1.get(i));
            saleProductRepository.save(saleProducts2.get(i));
        }

        // Delete all sale products from sale 1
        saleProductRepository.deleteAllBySaleId(sale1.getId());

        // get the sale products from the database
        List<SaleProduct> savedSaleProducts1 = saleProductRepository.findAllBySaleId(sale1.getId());

        // Assert that the sale products from sale 1 were deleted
        assertThat(savedSaleProducts1.size()).isEqualTo(0);

        // Get all the sales products from sale 2
        List<SaleProduct> savedSaleProducts2 = saleProductRepository.findAllBySaleId(sale2.getId());

        // Check that the sale products from sale 2 were not deleted
        assertThat(savedSaleProducts2).isEqualTo(saleProducts2);

    }

}
