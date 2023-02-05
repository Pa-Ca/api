package com.paca.paca.product.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ProductDTO {

    private Long id;
    private Long productSubCategoryId;
    private Boolean disabled;
    private String name;
    private Long price;
    private String description;
}
