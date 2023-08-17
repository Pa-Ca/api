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
    private Short type;
    private String name;
    private Float value;
}
