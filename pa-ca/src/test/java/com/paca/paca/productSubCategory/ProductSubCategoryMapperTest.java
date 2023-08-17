package com.paca.paca.productSubCategory;

import org.mockito.InjectMocks;
import org.junit.jupiter.api.Test;
import com.paca.paca.utils.TestUtils;
import com.paca.paca.branch.model.Branch;
import com.paca.paca.productSubCategory.model.ProductCategory;
import com.paca.paca.productSubCategory.dto.ProductCategoryDTO;
import com.paca.paca.productSubCategory.model.ProductSubCategory;
import com.paca.paca.productSubCategory.dto.ProductSubCategoryDTO;
import com.paca.paca.productSubCategory.utils.ProductCategoryMapperImpl;
import com.paca.paca.productSubCategory.utils.ProductSubCategoryMapperImpl;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
public class ProductSubCategoryMapperTest {

    @InjectMocks
    private ProductCategoryMapperImpl productCategoryMapper;

    @InjectMocks
    private ProductSubCategoryMapperImpl productSubCategoryMapper;

    private TestUtils utils = TestUtils.builder().build();

    @Test
    void shouldMapProductSubCategoryEntityToProductSubCategoryDTO() {
        ProductSubCategory productSubCategory = utils.createProductSubCategory(null, null);

        ProductSubCategoryDTO response = productSubCategoryMapper.toDTO(productSubCategory);
        ProductSubCategoryDTO expected = new ProductSubCategoryDTO(
                productSubCategory.getId(),
                productSubCategory.getBranch().getId(),
                productSubCategory.getCategory().getId(),
                productSubCategory.getName());

        assertThat(response).isEqualTo(expected);
    }

    @Test
    void shouldMapProductSubCategoryDTOtoProductSubCategoryEntity() {
        Branch branch = utils.createBranch(null);
        ProductCategory category = utils.createProductCategory();
        ProductSubCategoryDTO dto = utils.createProductSubCategoryDTO(utils.createProductSubCategory(branch, category));

        ProductSubCategory response = productSubCategoryMapper.toEntity(dto, branch, category);
        ProductSubCategory expected = new ProductSubCategory(
                dto.getId(),
                branch,
                category,
                dto.getName());

        assertThat(response).isEqualTo(expected);
    }

    @Test
    void shouldPartiallyMapProductSubCategoryDTOtoProductSubCategoryEntity() {
        Branch branch = utils.createBranch(null);
        ProductCategory category = utils.createProductCategory();
        ProductSubCategory productSubCategory = utils.createProductSubCategory(branch, category);
        ProductSubCategoryDTO dto = utils.createProductSubCategoryDTO(productSubCategory);

        ProductSubCategory response = productSubCategoryMapper.toEntity(dto, branch, category);
        ProductSubCategory expected = new ProductSubCategory(
                productSubCategory.getId(),
                branch,
                category,
                dto.getName());

        assertThat(response).isEqualTo(expected);

    }

    @Test
    void shouldMapProductCategoryEntityToProductCategoryDTO() {
        ProductCategory productCategory = utils.createProductCategory();

        ProductCategoryDTO response = productCategoryMapper.toDTO(productCategory);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(productCategory.getId());
        assertThat(response.getName()).isEqualTo(productCategory.getName());
    }

    @Test
    void shouldMapProductCategoryDTOtoProductCategoryEntity() {
        ProductCategoryDTO dto = utils.createProductCategoryDTO(utils.createProductCategory());

        ProductCategory productCategory = productCategoryMapper.toEntity(dto);

        assertThat(productCategory).isNotNull();
        assertThat(productCategory.getId()).isEqualTo(dto.getId());
        assertThat(productCategory.getName()).isEqualTo(dto.getName());
    }

    @Test
    void shouldPartiallyMapProductCategoryDTOtoProductCategoryEntity() {
        ProductCategory productCategory = utils.createProductCategory();
        ProductCategoryDTO dto = utils.createProductCategoryDTO(productCategory);

        ProductCategory response = productCategoryMapper.toEntity(dto);
        ProductCategory expected = new ProductCategory(
                productCategory.getId(),
                dto.getName());

        assertThat(response).isEqualTo(expected);
    }

}
