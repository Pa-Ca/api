package com.paca.paca.sale.dto;

import lombok.*;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class TaxDTO {

    private Long id;
    private Short type;
    private String name;
    private Float value;
}
