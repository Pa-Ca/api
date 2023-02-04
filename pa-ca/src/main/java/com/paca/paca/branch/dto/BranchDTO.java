package com.paca.paca.branch.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class BranchDTO {

    private Long id;
    private Long businessId;
    private String address;
    private String coordinates;
    private String name;
    private String overview;
    private Float score;
    private Integer capacity;
    private Long reservationPrice;
    private Boolean reserveOff;
}
