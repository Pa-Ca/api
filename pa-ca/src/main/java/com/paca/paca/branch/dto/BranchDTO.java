package com.paca.paca.branch.dto;

import lombok.*;

import java.time.Duration;
import java.time.LocalTime;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class BranchDTO {

    protected Long id;
    protected Long businessId;
    protected String name;
    protected Float score;
    protected Short capacity;
    protected BigDecimal reservationPrice;
    protected String mapsLink;
    protected String location;
    protected String overview;
    protected Boolean visibility;
    protected Boolean reserveOff;
    protected String phoneNumber;
    protected String type;
    protected LocalTime hourIn;
    protected LocalTime hourOut;
    protected Duration averageReserveTime;
    protected Float dollarExchange;
    protected Boolean deleted;

}
