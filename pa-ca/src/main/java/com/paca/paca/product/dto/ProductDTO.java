package com.paca.paca.product.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

    private Long id;
    private Long subCategoryId;
    private String name;
    private BigDecimal price;
    private String description;
    private Boolean disabled;
}
