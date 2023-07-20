package com.paca.paca.productSubCategory.utils;

import org.mapstruct.*;

import com.paca.paca.productSubCategory.dto.ProductCategoryDTO;
import com.paca.paca.productSubCategory.model.ProductCategory;

@Mapper(componentModel = "spring")
public interface ProductCategoryMapper {

    ProductCategoryDTO toDTO(ProductCategory productCategory);

    ProductCategory toEntity(ProductCategoryDTO dto);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ProductCategory updateModel(ProductCategoryDTO dto, @MappingTarget ProductCategory category);
}