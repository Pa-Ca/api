package com.paca.paca.product.utils;

import org.mapstruct.*;
import com.paca.paca.product.model.Product;
import com.paca.paca.product.dto.ProductDTO;
import com.paca.paca.product_sub_category.model.ProductSubCategory;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(source = "subCategory.id", target = "subCategoryId")
    ProductDTO toDTO(Product product);

    @Mapping(target = "subCategory", ignore = true)
    Product toEntity(ProductDTO dto);

    default Product toEntity(ProductDTO dto, ProductSubCategory subCategory) {
        Product product = toEntity(dto);
        product.setSubCategory(subCategory);

        return product;
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "subCategory", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Product updateModel(ProductDTO dto, @MappingTarget Product product);
}