package com.paca.paca.product_sub_category.utils;

import org.mapstruct.*;

import com.paca.paca.product_sub_category.model.ProductSubCategory;
import com.paca.paca.product_sub_category.dto.ProductSubCategoryDTO;

@Mapper(componentModel = "spring")
public interface ProductSubCategoryMapper {
    ProductSubCategoryDTO toDTO(ProductSubCategory productCategory);

    ProductSubCategory toEntity(ProductSubCategoryDTO dto);

    default ProductSubCategory updateModel(
            ProductSubCategory subCategory,
            ProductSubCategoryDTO dto) {
        if (dto.getName() != null) {
            subCategory.setName(dto.getName());
        }

        return subCategory;
    }
}