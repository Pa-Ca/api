package com.paca.paca.productSubCategory;

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
import com.paca.paca.productSubCategory.model.ProductCategory;
import com.paca.paca.productSubCategory.model.ProductSubCategory;
import com.paca.paca.productSubCategory.repository.ProductCategoryRepository;
import com.paca.paca.productSubCategory.repository.ProductSubCategoryRepository;

import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.util.List;

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
    void shouldGetAllProductSubCategoriesByBranchId() {
        int nProductSubCategories = 10;
        Branch branch = utils.createBranch(null);

        for (int i = 0; i < nProductSubCategories; i++) {
            utils.createProductSubCategory(branch, null);
            utils.createProductSubCategory(null, null);
        }

        List<ProductSubCategory> productSubCategories = productSubCategoryRepository
                .findAllByBranchId(branch.getId());

        assertThat(productSubCategories.size()).isEqualTo(nProductSubCategories);
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
    void shouldCheckThatProductSubCategoryExistsByBranchIdAndCategoryIdAndName() {
        Branch branch = utils.createBranch(null);
        ProductCategory productCategory = utils.createProductCategory();
        ProductSubCategory productSubCategory = utils.createProductSubCategory(branch, productCategory);

        boolean expected = productSubCategoryRepository.existsByBranchIdAndCategoryIdAndName(
                productSubCategory.getBranch().getId(),
                productSubCategory.getCategory().getId(),
                productSubCategory.getName());

        assertThat(expected).isTrue();
    }

    @Test
    void shouldCheckThatProductSubCategoryShouldNotExistsByBranchIdAndCategoryIdAndName() {
        Branch branch = utils.createBranch(null);
        ProductCategory productCategory = utils.createProductCategory();
        ProductSubCategory productSubCategory = utils.createProductSubCategory(branch, productCategory);

        boolean expected = productSubCategoryRepository.existsByBranchIdAndCategoryIdAndName(
                productSubCategory.getBranch().getId(),
                productSubCategory.getCategory().getId(),
                productSubCategory.getName() + "1");

        assertThat(expected).isFalse();
    }

}
