package com.paca.paca.productSubCategory.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductSubCategoryDTO {

    private Long id;
    private Long branchId;
    private Long categoryId;
    private String name;
}
