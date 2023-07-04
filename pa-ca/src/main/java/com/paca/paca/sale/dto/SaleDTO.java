package com.paca.paca.sale.dto;

import java.math.BigDecimal;
import java.util.Date;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SaleDTO {
    private Long id;
    private Integer clientQuantity;
    private Date startTime;
    private Date endTime;
    private Integer status;
    private Long tableId;
    private String tableName;
    private String note;
    private BigDecimal dollarToLocalCurrencyExchange;
    private Long reservationId;
}