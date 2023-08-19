package com.paca.paca.sale.dto;

import java.util.Date;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SaleDTO {

    private Long id;
    private Long branchId;
    private Long clientGuestId;
    private Long invoiceId;
    private Short clientQuantity;
    private Short status;
    private Date startTime;
    private Date endTime;
    private Float dollarExchange;
    private String note;
}