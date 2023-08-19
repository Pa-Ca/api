package com.paca.paca.sale.dto;

import lombok.*;
import java.util.List;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class BranchSalesInfoDTO {

    List<SaleInfoDTO> ongoingSalesInfo;
    List<SaleInfoDTO> historicSalesInfo;
    int currentHistoricPage;
    int totalHistoricPages;
    int totalHistoricElements;
}
