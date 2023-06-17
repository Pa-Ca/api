package com.paca.paca.sale.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaxDTO {
    private Long id;
    private String name;
    private Integer type;
    private Float value;
    private Long saleId;
}
