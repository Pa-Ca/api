package com.paca.paca.product.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@EqualsAndHashCode
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
