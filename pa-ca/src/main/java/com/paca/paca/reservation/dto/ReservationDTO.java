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
public class ReservationDTO {

    private Long id;
    private Long branchId;
    private Long guestId;
    private Long invoiceId;
    private Date requestDate;
    private Date reservationDateIn;
    private Date reservationDateOut;
    private BigDecimal price;
    private Short status;
    private Short tableNumber;
    private Short clientNumber;
    private String occasion;
    private Boolean byClient;
}