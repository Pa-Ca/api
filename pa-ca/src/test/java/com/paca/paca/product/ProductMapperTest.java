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
        ProductDTO expected = new ProductDTO(
                product.getId(),
                product.getSubCategory().getId(),
                product.getName(),
                product.getPrice(),
                product.getDescription(),
                product.getDisabled());

        assertThat(response).isEqualTo(expected);
    }

    @Test
    void shouldMapProductDTOtoProductEntity() {
        ProductSubCategory subCategory = utils.createProductSubCategory(null, null);
        Product product = utils.createProduct(subCategory);
        ProductDTO dto = utils.createProductDTO(product);

        Product response = productMapper.toEntity(dto, subCategory);
        Product expected = new Product(
                dto.getId(),
                subCategory,
                dto.getName(),
                dto.getPrice(),
                dto.getDescription(),
                dto.getDisabled());

        assertThat(response).isEqualTo(expected);
    }

    @Test
    void shouldPartiallyMapProductDTOtoProductEntity() {
        ProductSubCategory subCategory = utils.createProductSubCategory(null, null);
        Product product = utils.createProduct(subCategory);

        ProductDTO dto = new ProductDTO(
                product.getId() + 1,
                subCategory.getId() + 1,
                product.getName() + ".",
                product.getPrice().add(BigDecimal.valueOf(1)),
                product.getDescription() + ".",
                !product.getDisabled());
        Product response = productMapper.updateModel(dto, product);
        Product expected = new Product(
                product.getId(),
                subCategory,
                dto.getName(),
                dto.getPrice(),
                dto.getDescription(),
                dto.getDisabled());

        assertThat(response).isEqualTo(expected);
    }
}
