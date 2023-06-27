package com.paca.paca.sale.dto;

import java.util.List;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SaleInfoDTO {
    SaleDTO sale;
    List<TaxDTO> taxes;
    List<SaleProductDTO> products;
}
