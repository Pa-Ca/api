package com.paca.paca.productSubCategory;

import org.junit.jupiter.api.Test;

import com.paca.paca.RepositoryTest;
import com.paca.paca.branch.model.Branch;
import com.paca.paca.productSubCategory.model.ProductCategory;
import com.paca.paca.productSubCategory.model.ProductSubCategory;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ProductSubCategoryRepositoryTest extends RepositoryTest {

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
