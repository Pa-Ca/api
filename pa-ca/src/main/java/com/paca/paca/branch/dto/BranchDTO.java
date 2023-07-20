package com.paca.paca.branch.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.Duration;
import java.time.LocalTime;
import java.math.BigDecimal;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BranchDTO {

    protected Long id;
    protected Long businessId;
    protected String location;
    protected String mapsLink;
    protected String name;
    protected String overview;
    protected Float score;
    protected Integer capacity;
    protected BigDecimal reservationPrice;
    protected Boolean reserveOff;
    protected Duration averageReserveTime;
    protected Boolean visibility;
    protected String phoneNumber;
    protected String type;
    protected LocalTime hourIn;
    protected LocalTime hourOut;
    protected Boolean deleted;
    protected BigDecimal dollarToLocalCurrencyExchange;

}
