package com.paca.paca.productSubCategory.utils;

import org.mapstruct.*;

import com.paca.paca.branch.model.Branch;
import com.paca.paca.productSubCategory.model.ProductCategory;
import com.paca.paca.productSubCategory.model.ProductSubCategory;
import com.paca.paca.productSubCategory.dto.ProductSubCategoryDTO;

@Mapper(componentModel = "spring")
public interface ProductSubCategoryMapper {

    @Mapping(source = "branch.id", target = "branchId")
    @Mapping(source = "category.id", target = "categoryId")
    ProductSubCategoryDTO toDTO(ProductSubCategory productCategory);

    @Mapping(target = "branch", ignore = true)
    @Mapping(target = "category", ignore = true)
    ProductSubCategory toEntity(ProductSubCategoryDTO dto);

    default ProductSubCategory toEntity(ProductSubCategoryDTO dto, Branch branch, ProductCategory category) {
        ProductSubCategory subCategory = toEntity(dto);
        subCategory.setBranch(branch);
        subCategory.setCategory(category);

        return subCategory;
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "branch", ignore = true)
    @Mapping(target = "category", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ProductSubCategory updateModel(ProductSubCategoryDTO dto, @MappingTarget ProductSubCategory subCategory);
}