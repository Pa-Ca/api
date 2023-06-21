package com.paca.paca.sale.dto;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SaleInfoDTO {
    SaleDTO sale;
    TaxListDTO taxes;
    SaleProductListDTO products;
}
