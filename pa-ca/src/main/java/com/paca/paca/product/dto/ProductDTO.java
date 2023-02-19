package com.paca.paca.product.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ProductDTO {

    private Long id;
    private Long subCategoryId;
    private Boolean disabled;
    private String name;
    private Float price;
    private String description;
}
