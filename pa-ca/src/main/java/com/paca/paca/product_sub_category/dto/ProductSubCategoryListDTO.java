package com.paca.paca.product_sub_category.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductSubCategoryListDTO {
    List<ProductSubCategoryDTO> productSubCategories;
}
