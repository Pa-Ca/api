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
    private Date requestDate;
    private Date reservationDate;
    private Integer clientNumber;
    private String payment;
    private Integer status;
    private Date payDate;
    private BigDecimal price;
    private String occasion;
    private Boolean byClient;
    private Boolean haveGuest;
    private String name;
    private String surname;
    private String email;
    private String phoneNumber;
}
