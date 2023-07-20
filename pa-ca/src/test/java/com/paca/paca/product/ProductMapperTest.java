package com.paca.paca.product;

import org.mockito.InjectMocks;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.paca.paca.utils.TestUtils;
import com.paca.paca.product.model.Product;
import com.paca.paca.product.dto.ProductDTO;
import com.paca.paca.product.utils.ProductMapperImpl;
import com.paca.paca.productSubCategory.model.ProductSubCategory;
import com.paca.paca.productSubCategory.utils.ProductSubCategoryMapperImpl;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.math.BigDecimal;

@ExtendWith(SpringExtension.class)
public class ProductMapperTest {
        @InjectMocks
        private ProductMapperImpl productMapper;

        @InjectMocks
        private ProductSubCategoryMapperImpl productSubCategoryMapper;

        private TestUtils utils = TestUtils.builder().build();

        @Test
        void shouldMapProductEntityToProductDTO() {
                Product product = utils.createProduct(null);

                ProductDTO response = productMapper.toDTO(product);

                assertThat(response).isNotNull();
                assertThat(response.getId()).isEqualTo(product.getId());
                assertThat(response.getId()).isEqualTo(product.getId());
                assertThat(response.getSubCategoryId()).isEqualTo(product.getSubCategory().getId());
                assertThat(response.getDisabled()).isEqualTo(product.getDisabled());
                assertThat(response.getName()).isEqualTo(product.getName());
                assertThat(response.getPrice()).isEqualTo(product.getPrice());
                assertThat(response.getDescription()).isEqualTo(product.getDescription());
        }

        @Test
        void shouldMapProductDTOtoProductEntity() {
                ProductDTO dto = utils.createProductDTO(null);

                Product product = productMapper.toEntity(dto);

                assertThat(product).isNotNull();
                assertThat(product.getId()).isEqualTo(dto.getId());
                assertThat(product.getSubCategory()).isNull(); // Ignore subcategory
                assertThat(product.getDisabled()).isEqualTo(dto.getDisabled());
                assertThat(product.getName()).isEqualTo(dto.getName());
                assertThat(product.getPrice()).isEqualTo(dto.getPrice());
                assertThat(product.getDescription()).isEqualTo(dto.getDescription());
        }

        @Test
        void shouldMapProductDTOWithProductSubcategoryEntityToProductEntity() {
                ProductSubCategory productSubCategory = utils.createProductSubCategory(null, null);
                ProductDTO dto = utils.createProductDTO(utils.createProduct(productSubCategory));

                Product product = productMapper.toEntity(dto, productSubCategory);

                assertThat(product).isNotNull();
                assertThat(product.getId()).isEqualTo(dto.getId());
                assertThat(product.getSubCategory().getId()).isEqualTo(productSubCategory.getId());
                assertThat(product.getDisabled()).isEqualTo(dto.getDisabled());
                assertThat(product.getName()).isEqualTo(dto.getName());
                assertThat(product.getPrice()).isEqualTo(dto.getPrice());
                assertThat(product.getDescription()).isEqualTo(dto.getDescription());
        }

        @Test
        void shouldPartiallyMapProductDTOtoProductEntity() {
                Product product = utils.createProduct(null);

                // Not changing ID
                ProductDTO dto = ProductDTO.builder()
                                .id(product.getId() + 1)
                                .build();
                Product updatedproduct = productMapper.updateModel(dto, product);
                assertThat(updatedproduct).isNotNull();
                assertThat(updatedproduct.getId()).isEqualTo(product.getId());

                // Not changing ProductSubCategory ID
                dto = ProductDTO.builder()
                                .subCategoryId(product.getSubCategory().getId() + 1)
                                .build();
                updatedproduct = productMapper.updateModel(dto, product);
                assertThat(updatedproduct).isNotNull();
                assertThat(updatedproduct.getSubCategory().getId()).isEqualTo(product.getSubCategory().getId());

                // Changing disabled
                dto = ProductDTO.builder()
                                .disabled(true)
                                .build();
                updatedproduct = productMapper.updateModel(dto, product);
                assertThat(updatedproduct).isNotNull();
                assertThat(updatedproduct.getDisabled()).isTrue();

                // Changing name
                dto = ProductDTO.builder()
                                .name("new name")
                                .build();
                updatedproduct = productMapper.updateModel(dto, product);
                assertThat(updatedproduct).isNotNull();
                assertThat(updatedproduct.getName()).isEqualTo(dto.getName());

                // Changing price
                dto = ProductDTO.builder()
                                .price(product.getPrice().add(BigDecimal.valueOf(1)))
                                .build();
                updatedproduct = productMapper.updateModel(dto, product);
                assertThat(updatedproduct).isNotNull();
                assertThat(updatedproduct.getPrice()).isEqualTo(dto.getPrice());

                // Changing description
                dto = ProductDTO.builder()
                                .description("new description")
                                .build();
                updatedproduct = productMapper.updateModel(dto, product);
                assertThat(updatedproduct).isNotNull();
                assertThat(updatedproduct.getDescription()).isEqualTo(dto.getDescription());
        }
}
