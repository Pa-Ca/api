package com.paca.paca.sale.dto;

import java.math.BigDecimal;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SaleProductDTO {
    private Long id;
    private String name;
    private Long saleId;
    private Long productId;
    private Integer amount;
    private BigDecimal price;
}