package com.paca.paca.reservation.dto;

import lombok.*;
import java.util.Date;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDTO {

    private Long id;
    private Date payDate;
    private BigDecimal price;
    private String payment;
    private String paymentCode;
}
