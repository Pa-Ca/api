package com.paca.paca.reservation.dto;

import lombok.*;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class GuestDTO {

    private Long id;
    private String name;
    private String surname;
    private String email;
    private String phoneNumber;
    private String identityDocument;
}