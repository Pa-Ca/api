package com.paca.paca.product_sub_category.utils;

import org.mapstruct.*;

import com.paca.paca.product_sub_category.model.ProductSubCategory;
import com.paca.paca.product_sub_category.dto.ProductSubCategoryDTO;

@Mapper(componentModel = "spring")
public interface ProductSubCategoryMapper {

    @Mapping(source = "branch.id", target = "branchId")
    @Mapping(source = "category.id", target = "categoryId")
    ProductSubCategoryDTO toDTO(ProductSubCategory productCategory);

    ProductSubCategory toEntity(ProductSubCategoryDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "branch", ignore = true)
    @Mapping(target = "category", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ProductSubCategory updateModel(ProductSubCategoryDTO dto, @MappingTarget ProductSubCategory subCategory);
}