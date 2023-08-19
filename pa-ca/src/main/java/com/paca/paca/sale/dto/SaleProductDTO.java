package com.paca.paca.sale.dto;

import java.math.BigDecimal;

import lombok.*;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class SaleProductDTO {

    private Long id;
    private Long saleId;
    private Long productId;
    private String name;
    private Integer amount;
    private BigDecimal price;
}