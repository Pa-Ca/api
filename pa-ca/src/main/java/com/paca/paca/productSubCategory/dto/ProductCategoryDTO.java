package com.paca.paca.productSubCategory.dto;

import lombok.*;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ProductCategoryDTO {

    private Long id;
    private String name;
}