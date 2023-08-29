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