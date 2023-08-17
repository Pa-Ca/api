package com.paca.paca.productSubCategory.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductSubCategoryListDTO {

    List<ProductSubCategoryDTO> productSubCategories;
}
