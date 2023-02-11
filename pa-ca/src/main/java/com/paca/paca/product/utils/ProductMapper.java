package com.paca.paca.product.utils;

import com.paca.paca.product.model.Product;
import com.paca.paca.product.dto.ProductDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDTO toDTO(Product product);

    Product toEntity(ProductDTO dto);

    default Product updateModel(Product product, ProductDTO dto) {
        if (dto.getDisabled() != null) {
            product.setDisabled(dto.getDisabled());
        }
        if (dto.getName() != null) {
            product.setName(dto.getName());
        }
        if (dto.getPrice() != null) {
            product.setPrice(dto.getPrice());
        }
        if (dto.getDescription() != null) {
            product.setDescription(dto.getDescription());
        }

        return product;
    }
}