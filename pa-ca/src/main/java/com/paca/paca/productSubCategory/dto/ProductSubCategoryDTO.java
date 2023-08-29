package com.paca.paca.productSubCategory.dto;

import lombok.*;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ProductSubCategoryDTO {

    private Long id;
    private Long branchId;
    private Long categoryId;
    private String name;
}
