package com.paca.paca.sale.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BranchSalesDTO {
    List<SaleDTO> ongoingSales;
    List<SaleDTO> historicSales; // Historic sales are sales that have already been closed or canceled
}
