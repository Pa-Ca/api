package com.paca.paca.sale.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

import lombok.AllArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BranchSalesInfoDTO {

    List<SaleInfoDTO> ongoingSalesInfo;
    List<SaleInfoDTO> historicSalesInfo;
    int currentHistoricPage;
    int totalHistoricPages;
    int totalHistoricElements;
}
