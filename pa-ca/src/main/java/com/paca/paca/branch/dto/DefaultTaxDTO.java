package com.paca.paca.branch.dto;

import lombok.*;

import com.paca.paca.sale.dto.TaxDTO;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class DefaultTaxDTO {

    private Long id;
    private Long branchId;
    private TaxDTO tax;
}
