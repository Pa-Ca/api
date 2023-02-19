package com.paca.paca.reservation.dto;

import java.util.Date;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ReservationDTO {

    private Long id;
    private Long branchId;
    private Date requestDate;
    private Date reservationDate;
    private Integer clientNumber;
    private String payment;
    private Integer status;
    private Date payDate;
    private Float price;
}
