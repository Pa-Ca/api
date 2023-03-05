package com.paca.paca.product;

import com.paca.paca.branch.model.Branch;
import com.paca.paca.branch.repository.BranchRepository;
import com.paca.paca.business.model.Business;
import com.paca.paca.business.repository.BusinessRepository;
import com.paca.paca.client.model.Client;
import com.paca.paca.product.model.Product;
import com.paca.paca.product.repository.ProductRepository;
import com.paca.paca.product_sub_category.model.ProductSubCategory;
import com.paca.paca.product_sub_category.repository.ProductCategoryRepository;
import com.paca.paca.product_sub_category.repository.ProductSubCategoryRepository;
import com.paca.paca.user.model.User;
import com.paca.paca.user.repository.RoleRepository;
import com.paca.paca.user.repository.UserRepository;
import com.paca.paca.utils.TestUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@Testcontainers
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProductRepositoryTest {
    @Autowired private UserRepository userRepository;
    @Autowired private RoleRepository roleRepository;
    @Autowired private BusinessRepository businessRepository;
    @Autowired private BranchRepository branchRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private ProductCategoryRepository productCategoryRepository;
    @Autowired private ProductSubCategoryRepository productSubCategoryRepository;

    private TestUtils utils;

    @BeforeAll
    void initUtils() {
        utils = TestUtils.builder()
            .roleRepository(roleRepository)
            .userRepository(userRepository)
            .productRepository(productRepository)
            .productCategoryRepository(productCategoryRepository)
            .productSubCategoryRepository(productSubCategoryRepository)
            .branchRepository(branchRepository)
            .businessRepository(businessRepository)
            .build();
    }

    @BeforeEach
    void restoreClientDB() {
        productRepository.deleteAll();
    }

    @AfterEach
    void restoreTest() {
        productRepository.deleteAll();
    }

    @Test
    void shouldCreateProduct() {
        Product product = utils.createProduct(null);

        Product savedProduct = productRepository.save(product);

        assertThat(savedProduct).isNotNull();
        assertThat(savedProduct.getId()).isEqualTo(product.getId());
        assertThat(savedProduct.getName()).isEqualTo(product.getName());
        assertThat(savedProduct.getPrice()).isEqualTo(product.getPrice());
        assertThat(savedProduct.getDisabled()).isEqualTo(product.getDisabled());
        assertThat(savedProduct.getDescription()).isEqualTo(product.getDescription());
        assertThat(savedProduct.getSubCategory().getId()).isEqualTo(product.getSubCategory().getId());
    }

    @Test
    void shouldGetAllClients() {
        int nProducts = 10;

        for (int i = 0; i < nProducts; i++) {
            utils.createProduct(null);
        }

        List<Product> products = productRepository.findAll();

        assertThat(products.size()).isEqualTo(nProducts);
    }

    @Test
    void shouldCheckThatProductExistsById() {
        Product product = utils.createProduct(null);

        boolean expected = productRepository.existsById(product.getId());
        Optional<Product> retrievedProduct = productRepository.findById(product.getId());

        assertThat(expected).isTrue();
        assertThat(retrievedProduct.isPresent()).isTrue();
        assertThat(retrievedProduct.get().getId()).isEqualTo(product.getId());
        assertThat(retrievedProduct.get().getSubCategory().getId()).isEqualTo(product.getSubCategory().getId());
        assertThat(retrievedProduct.get().getDisabled()).isEqualTo(product.getDisabled());
        assertThat(retrievedProduct.get().getName()).isEqualTo(product.getName());
        assertThat(retrievedProduct.get().getPrice()).isEqualTo(product.getPrice());
        assertThat(retrievedProduct.get().getDescription()).isEqualTo(product.getDescription());
    }

    @Test
    void shouldCheckThatProductDoesNotExistsById() {
        boolean expected = productRepository.existsById(1L);
        Optional<Product> retrievedProduct = productRepository.findById(1L);

        assertThat(expected).isFalse();
        assertThat(retrievedProduct.isPresent()).isFalse();
    }

    @Test
    void shouldFindAllProductsBySubCategoryId() {
        int nProducts = 10;
        ProductSubCategory productSubCategory = utils.createProductSubCategory(null,null);

        for (int i = 0; i < nProducts; i++) {
            utils.createProduct(productSubCategory);
        }

        List<Product> products = productRepository.findAllBySubCategoryId(productSubCategory.getId());

        assertThat(products.size()).isEqualTo(nProducts);
    }

    @Test
    void shouldNotFindProductsBySubCategoryId() {
        List<Product> products = productRepository.findAllBySubCategoryId(1L);

        assertThat(products.size()).isEqualTo(0);
    }

    @Test
    void shouldFindAllProductsBySubCategory_Branch_Id() {
        int nProducts = 10;
        Branch branch = utils.createBranch(null);
        ProductSubCategory productSubCategory = utils.createProductSubCategory(branch,null);

        for (int i = 0; i < nProducts; i++) {
            utils.createProduct(productSubCategory);
        }

        List<Product> products = productRepository.findAllBySubCategory_Branch_Id(productSubCategory.getBranch().getId());

        assertThat(products.size()).isEqualTo(nProducts);
    }

    @Test
    void shouldNotFindProductsBySubCategory_Branch_Id() {
        List<Product> products = productRepository.findAllBySubCategory_Branch_Id(1L);

        assertThat(products.size()).isEqualTo(0);
    }

    @Test
    void shouldCheckThatProductExistsByIdAndSubCategory_Branch_Business_Id() {
        Business business = utils.createBusiness(null);
        Branch branch = utils.createBranch(business);
        ProductSubCategory productSubCategory = utils.createProductSubCategory(branch,null);
        Product product = utils.createProduct(productSubCategory);

        boolean expected = productRepository.existsByIdAndSubCategory_Branch_Business_Id(
                product.getId(), business.getId());
        Optional<Product> retrievedProduct = productRepository.findById(product.getId());

        assertThat(expected).isTrue();
        assertThat(retrievedProduct.isPresent()).isTrue();
        assertThat(retrievedProduct.get().getId()).isEqualTo(product.getId());
        assertThat(retrievedProduct.get().getSubCategory().getId()).isEqualTo(productSubCategory.getId());
        assertThat(retrievedProduct.get().getSubCategory().getBranch().getId()).isEqualTo(branch.getId());
        assertThat(retrievedProduct.get().getSubCategory().getBranch().getBusiness().getId()).isEqualTo(business.getId());
        assertThat(retrievedProduct.get().getDisabled()).isEqualTo(product.getDisabled());
        assertThat(retrievedProduct.get().getName()).isEqualTo(product.getName());
        assertThat(retrievedProduct.get().getPrice()).isEqualTo(product.getPrice());
        assertThat(retrievedProduct.get().getDescription()).isEqualTo(product.getDescription());
    }

    @Test
    void shouldCheckThatProductDoesNotExistsByIdAndSubCategory_Branch_Business_Id() {
        boolean expected = productRepository.existsByIdAndSubCategory_Branch_Business_Id(
                1L, 1L);
        Optional<Product> retrievedProduct = productRepository.findById(1L);

        assertThat(expected).isFalse();
        assertThat(retrievedProduct.isPresent()).isFalse();
    }

}
