package com.paca.paca.branch.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

import com.paca.paca.sale.dto.TaxDTO;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BranchInfoDTO {

    private BranchDTO branch;
    private List<TaxDTO> defaultTaxes;

}
