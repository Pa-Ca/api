package com.paca.paca.product_sub_category;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;

import com.paca.paca.PacaTest;
import com.paca.paca.utils.TestUtils;
import com.paca.paca.branch.model.Branch;
import com.paca.paca.user.repository.RoleRepository;
import com.paca.paca.user.repository.UserRepository;
import com.paca.paca.branch.repository.BranchRepository;
import com.paca.paca.business.repository.BusinessRepository;
import com.paca.paca.product_sub_category.model.ProductCategory;
import com.paca.paca.product_sub_category.model.ProductSubCategory;
import com.paca.paca.product_sub_category.repository.ProductCategoryRepository;
import com.paca.paca.product_sub_category.repository.ProductSubCategoryRepository;

import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@Testcontainers
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProductSubCategoryRepositoryTest extends PacaTest {
    
    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Autowired
    private ProductSubCategoryRepository productSubCategoryRepository;

    @Autowired
    private RoleRepository roleRepository;

    private TestUtils utils;

    @BeforeAll
    void initUtils() {
        utils = TestUtils.builder()
                .roleRepository(roleRepository)
                .userRepository(userRepository)
                .businessRepository(businessRepository)
                .branchRepository(branchRepository)
                .productCategoryRepository(productCategoryRepository)
                .productSubCategoryRepository(productSubCategoryRepository)
                .build();
    }

    @BeforeEach
    void restoreBranchDB() {
        productSubCategoryRepository.deleteAll();
        productCategoryRepository.deleteAll();
    }

    @AfterEach
    void restoreTest() {
        productSubCategoryRepository.deleteAll();
        productCategoryRepository.deleteAll();
        branchRepository.deleteAll();
        businessRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    void shouldCreateProductSubCategory() {
        Branch branch = utils.createBranch(null);
        ProductCategory productCategory = utils.createProductCategory();
        ProductSubCategory productSubCategory = ProductSubCategory.builder()
                .id(1L)
                .branch(branch)
                .category(productCategory)
                .name("text name")
                .build();

        ProductSubCategory savedProductSubCategory = productSubCategoryRepository.save(productSubCategory);

        assertThat(savedProductSubCategory).isNotNull();
        assertThat(savedProductSubCategory.getBranch().getId()).isEqualTo(productSubCategory.getBranch().getId());
        assertThat(savedProductSubCategory.getCategory().getId()).isEqualTo(productSubCategory.getCategory().getId());
        assertThat(savedProductSubCategory.getName()).isEqualTo(productSubCategory.getName());
    }

    @Test
    void shouldGetAllProductSubCategories() {
        int nProductSubCategories = 10;

        for (int i = 0; i < nProductSubCategories; i++) {
            utils.createProductSubCategory(null, null);
        }

        List<ProductSubCategory> productSubCategories = productSubCategoryRepository.findAll();

        assertThat(productSubCategories.size()).isEqualTo(nProductSubCategories);
    }

    @Test
    void shouldGetAllProductSubCategoriesByBranchIdAndCategoryId() {
        int nProductSubCategories = 10;
        Branch branch = utils.createBranch(null);
        ProductCategory category = utils.createProductCategory();

        for (int i = 0; i < nProductSubCategories; i++) {
            utils.createProductSubCategory(branch, category);
            utils.createProductSubCategory(branch, null);
            utils.createProductSubCategory(null, category);
            utils.createProductSubCategory(null, null);
        }

        List<ProductSubCategory> productSubCategories = productSubCategoryRepository
                .findAllByBranchIdAndCategoryId(branch.getId(), category.getId());

        assertThat(productSubCategories.size()).isEqualTo(nProductSubCategories);
    }

    @Test
    void shouldCheckThatProductSubCategoryExistsById() {
        ProductSubCategory productSubCategory = utils.createProductSubCategory(null, null);

        boolean expected = productSubCategoryRepository.existsById(productSubCategory.getId());
        Optional<ProductSubCategory> expectedProductSubCategory = productSubCategoryRepository.findById(productSubCategory.getId());

        assertThat(expected).isTrue();
        assertThat(expectedProductSubCategory.isPresent()).isTrue();
        assertThat(expectedProductSubCategory.get().getBranch().getId()).isEqualTo(productSubCategory.getBranch().getId());
        assertThat(expectedProductSubCategory.get().getCategory().getId()).isEqualTo(productSubCategory.getCategory().getId());
    }

    @Test
    void shouldCheckThatProductSubCategoryDoesNotExistsById() {
        boolean expected = productSubCategoryRepository.existsById(1L);
        Optional<ProductSubCategory> expectedProductSubCategory = productSubCategoryRepository.findById(1L);

        assertThat(expected).isFalse();
        assertThat(expectedProductSubCategory.isEmpty()).isTrue();
    }

    @Test
    void shouldCheckThatProductSubCategoryExistsByIdAndBranch_Business_Id() {
        ProductSubCategory productSubCategory = utils.createProductSubCategory(null, null);

        boolean expected = productSubCategoryRepository.existsByIdAndBranch_Business_Id(productSubCategory.getId(),
                productSubCategory.getBranch().getBusiness().getId());

        assertThat(expected).isTrue();
    }

    @Test
    void shouldCheckThatProductSubCategoryDoesNotExistsByIdAndBranch_Business_Id() {
        ProductSubCategory productSubCategory = utils.createProductSubCategory(null, null);

        boolean expected = productSubCategoryRepository.existsByIdAndBranch_Business_Id(productSubCategory.getId(),
                productSubCategory.getBranch().getBusiness().getId() + 1);

        assertThat(expected).isFalse();
    }

    @Test
    void shouldDeleteProductSubCategory() {
        ProductSubCategory productSubCategory = utils.createProductSubCategory(null, null);

        productSubCategoryRepository.delete(productSubCategory);

        List<ProductSubCategory> productSubCategories = productSubCategoryRepository.findAll();
        assertThat(productSubCategories.size()).isEqualTo(0);
    }

    @Test
    void shouldGetAllProductCategories() {
        int nProductCategories = 10;

        for (int i = 0; i < nProductCategories; i++) {
            utils.createProductCategory();
        }

        List<ProductCategory> productCategories = productCategoryRepository.findAll();

        assertThat(productCategories.size()).isEqualTo(nProductCategories);
    }

}
