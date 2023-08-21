package com.paca.paca.sale.dto;

import lombok.*;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class SaleTaxDTO {

    private Long id;
    private Long saleId;
    private TaxDTO tax;
}
