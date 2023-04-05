package com.paca.paca.reservation.dto;

import java.util.Date;

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
    private Date requestDate;
    private Date reservationDate;
    private Integer clientNumber;
    private String payment;
    private Integer status;
    private Date payDate;
    private Float price;
    private String occasion;
    private String petition;
    private Boolean byClient;
}
