package com.paca.paca.sale.dto;

import java.util.List;

import com.paca.paca.branch.dto.TableDTO;
import com.paca.paca.client.dto.ClientDTO;
import com.paca.paca.reservation.dto.GuestDTO;

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
    GuestDTO guest;
    ClientDTO client;
    Long reservationId;
    List<TaxDTO> taxes;
    List<TableDTO> tables;
    List<SaleProductDTO> products;
}
