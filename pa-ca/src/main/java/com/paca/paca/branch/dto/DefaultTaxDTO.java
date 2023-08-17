package com.paca.paca.branch.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import com.paca.paca.sale.dto.TaxDTO;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DefaultTaxDTO {

    private Long id;
    private Long branchId;
    private TaxDTO tax;
}
