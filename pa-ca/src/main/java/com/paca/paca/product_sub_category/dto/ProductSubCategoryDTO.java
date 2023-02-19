package com.paca.paca.product_sub_category.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ProductSubCategoryDTO {

    private Long id;
    private Long branchId;
    private Long categoryId;
    private String name;
}
