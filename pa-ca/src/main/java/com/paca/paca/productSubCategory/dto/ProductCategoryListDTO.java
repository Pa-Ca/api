package com.paca.paca.productSubCategory.dto;

import lombok.*;

import java.util.List;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ProductCategoryListDTO {

    List<ProductCategoryDTO> productCategories;
}
