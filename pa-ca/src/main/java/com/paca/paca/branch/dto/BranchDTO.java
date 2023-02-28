package com.paca.paca.branch.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BranchDTO {

    private Long id;
    private Long businessId;
    private String address;
    private String coordinates;
    private String name;
    private String overview;
    private Float score;
    private Integer capacity;
    private Float reservationPrice;
    private Boolean reserveOff;
}
