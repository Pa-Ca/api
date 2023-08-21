package com.paca.paca.branch.dto;

import lombok.*;

import java.util.List;

import com.paca.paca.sale.dto.TaxDTO;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class BranchInfoDTO {

    private BranchDTO branch;
    private List<TaxDTO> defaultTaxes;

}
