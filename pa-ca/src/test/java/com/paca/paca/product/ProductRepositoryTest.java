package com.paca.paca.product;

import com.paca.paca.RepositoryTest;
import com.paca.paca.branch.model.Branch;
import com.paca.paca.product.model.Product;
import com.paca.paca.business.model.Business;
import com.paca.paca.productSubCategory.model.ProductSubCategory;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ProductRepositoryTest extends RepositoryTest {

    @Test
    void shouldFindAllProductsBySubCategoryId() {
        int nProducts = 10;
        ProductSubCategory productSubCategory = utils.createProductSubCategory(null, null);

        for (int i = 0; i < nProducts; i++) {
            utils.createProduct(productSubCategory);
        }

        List<Product> products = productRepository.findAllBySubCategoryId(productSubCategory.getId());

        assertThat(products.size()).isEqualTo(nProducts);
    }

    @Test
    void shouldFindAllProductsBySubCategory_Branch_Id() {
        int nProducts = 10;
        Branch branch = utils.createBranch(null);
        ProductSubCategory productSubCategory = utils.createProductSubCategory(branch, null);

        for (int i = 0; i < nProducts; i++) {
            utils.createProduct(productSubCategory);
        }

        List<Product> products = productRepository
                .findAllBySubCategory_Branch_Id(productSubCategory.getBranch().getId());

        assertThat(products.size()).isEqualTo(nProducts);
    }

    @Test
    void shouldCheckThatProductExistsByIdAndSubCategory_Branch_Business_Id() {
        Business business = utils.createBusiness(null);
        Branch branch = utils.createBranch(business);
        ProductSubCategory productSubCategory = utils.createProductSubCategory(branch, null);
        Product product = utils.createProduct(productSubCategory);

        boolean expected = productRepository.existsByIdAndSubCategory_Branch_Business_Id(
                product.getId(), business.getId());

        assertThat(expected).isTrue();
    }

    @Test
    void shouldCheckThatProductDoesNotExistsByIdAndSubCategory_Branch_Business_Id() {
        boolean expected = productRepository.existsByIdAndSubCategory_Branch_Business_Id(
                1L, 1L);
        Optional<Product> retrievedProduct = productRepository.findById(1L);

        assertThat(expected).isFalse();
        assertThat(retrievedProduct.isPresent()).isFalse();
    }

    @Test
    void shouldVerifyIfExistsBySubCategoryIdAndName() {
        ProductSubCategory productSubCategory = utils.createProductSubCategory(null, null);
        Product product = utils.createProduct(productSubCategory);

        boolean expected1 = productRepository.existsBySubCategoryIdAndName(
                productSubCategory.getId(), product.getName());
        boolean expected2 = productRepository.existsBySubCategoryIdAndName(
                productSubCategory.getId(), product.getName() + "1");
        boolean expected3 = productRepository.existsBySubCategoryIdAndName(
                productSubCategory.getId() + 1, product.getName());

        assertThat(expected1).isTrue();
        assertThat(expected2).isFalse();
        assertThat(expected3).isFalse();
    }

}
