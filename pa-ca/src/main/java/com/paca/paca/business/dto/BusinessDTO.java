package com.paca.paca.business.dto;

import lombok.*;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class BusinessDTO {

    private Long id;
    private Long userId;
    private String email;
    private String tier;
    private String name;
    private Boolean verified;
    private String phoneNumber;
}