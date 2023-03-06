package com.paca.paca.product_sub_category.utils;

import org.mapstruct.*;

import com.paca.paca.product_sub_category.model.ProductCategory;
import com.paca.paca.product_sub_category.dto.ProductCategoryDTO;

@Mapper(componentModel = "spring")
public interface ProductCategoryMapper {

    ProductCategoryDTO toDTO(ProductCategory productCategory);

    ProductCategory toEntity(ProductCategoryDTO dto);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ProductCategory updateModel(ProductCategoryDTO dto, @MappingTarget ProductCategory category);
}