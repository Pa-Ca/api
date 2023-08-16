package com.paca.paca.reservation.dto;

import java.util.Date;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDTO {

    private Long id;
    private Date payDate;
    private BigDecimal price;
    private String payment;
    private String paymentCode;
}
