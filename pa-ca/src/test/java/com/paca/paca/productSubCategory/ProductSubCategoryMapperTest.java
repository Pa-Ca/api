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

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(productSubCategory.getId());
        assertThat(response.getBranchId()).isEqualTo(productSubCategory.getBranch().getId());
        assertThat(response.getCategoryId()).isEqualTo(productSubCategory.getCategory().getId());
        assertThat(response.getName()).isEqualTo(productSubCategory.getName());
    }

    @Test
    void shouldMapProductSubCategoryDTOtoProductSubCategoryEntity() {
        Branch branch = utils.createBranch(null);
        ProductCategory category = utils.createProductCategory();
        ProductSubCategoryDTO dto = utils.createProductSubCategoryDTO(utils.createProductSubCategory(branch, category));

        ProductSubCategory productSubCategory = productSubCategoryMapper.toEntity(dto, branch, category);

        assertThat(productSubCategory).isNotNull();
        assertThat(productSubCategory.getId()).isEqualTo(dto.getId());
        assertThat(productSubCategory.getBranch().getId()).isEqualTo(branch.getId());
        assertThat(productSubCategory.getCategory().getId()).isEqualTo(dto.getCategoryId());
        assertThat(productSubCategory.getName()).isEqualTo(dto.getName());
    }

    @Test
    void shouldPartiallyMapProductSubCategoryDTOtoProductSubCategoryEntity() {
        ProductSubCategory productSubCategory = utils.createProductSubCategory(null, null);

        // Not changing ID
        ProductSubCategoryDTO dto = ProductSubCategoryDTO.builder()
                .id(productSubCategory.getId() + 1)
                .build();
        ProductSubCategory updatedProductSubCategory = productSubCategoryMapper.updateModel(dto, productSubCategory);
        assertThat(updatedProductSubCategory).isNotNull();
        assertThat(updatedProductSubCategory.getId()).isEqualTo(productSubCategory.getId());

        // Not changing Branch ID
        dto = ProductSubCategoryDTO.builder()
                .branchId(productSubCategory.getBranch().getId() + 1)
                .build();
        updatedProductSubCategory = productSubCategoryMapper.updateModel(dto, productSubCategory);
        assertThat(updatedProductSubCategory).isNotNull();
        assertThat(updatedProductSubCategory.getBranch().getId()).isEqualTo(productSubCategory.getBranch().getId());

        // Not changing Category ID
        dto = ProductSubCategoryDTO.builder()
                .categoryId(productSubCategory.getCategory().getId() + 1)
                .build();
        updatedProductSubCategory = productSubCategoryMapper.updateModel(dto, productSubCategory);
        assertThat(updatedProductSubCategory).isNotNull();
        assertThat(updatedProductSubCategory.getCategory().getId()).isEqualTo(productSubCategory.getCategory().getId());

        // Changing name
        dto = ProductSubCategoryDTO.builder()
                .name("new name_test")
                .build();
        updatedProductSubCategory = productSubCategoryMapper.updateModel(dto, productSubCategory);
        assertThat(updatedProductSubCategory).isNotNull();
        assertThat(updatedProductSubCategory.getName()).isEqualTo(dto.getName());
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

        // Not changing ID
        ProductCategoryDTO dto = ProductCategoryDTO.builder()
                .id(productCategory.getId() + 1)
                .build();
        ProductCategory updatedProductCategory = productCategoryMapper.updateModel(dto, productCategory);
        assertThat(updatedProductCategory).isNotNull();
        assertThat(updatedProductCategory.getId()).isEqualTo(productCategory.getId());

        // Changing name
        dto = ProductCategoryDTO.builder()
                .name("new name_test")
                .build();
        updatedProductCategory = productCategoryMapper.updateModel(dto, productCategory);
        assertThat(updatedProductCategory).isNotNull();
        assertThat(updatedProductCategory.getName()).isEqualTo(dto.getName());
    }

}
