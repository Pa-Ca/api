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

    private Long id;
    private Long businessId;
    private String location;
    private String mapsLink;
    private String name;
    private String overview;
    private Float score;
    private Integer capacity;
    private BigDecimal reservationPrice;
    private Boolean reserveOff;
    private Duration averageReserveTime;
    private Boolean visibility;
    private String phoneNumber;
    private String type;
    private LocalTime hourIn;
    private LocalTime hourOut;
    private Boolean deleted;
}
