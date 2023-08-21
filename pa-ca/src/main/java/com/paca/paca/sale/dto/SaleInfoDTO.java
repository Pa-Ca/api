package com.paca.paca.sale.dto;

import java.util.List;

import com.paca.paca.branch.dto.TableDTO;

import lombok.*;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class SaleInfoDTO {

    SaleDTO sale;
    Boolean insite;
    Long reservationId;
    List<TaxDTO> taxes;
    List<TableDTO> tables;
    List<SaleProductDTO> products;
}
