package com.paca.paca.business.dto;

import lombok.*;

@Builder
@Getter
@Setter
public class BusinessDTO {
    private Long id;
    private Long userId;
    private String email;
    private String name;
    private Boolean verified;
    private String tier;
}