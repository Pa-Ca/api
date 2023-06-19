package com.paca.paca.reservation.dto;

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
public class GuestDTO {

    private Long id;
    private String name;
    private String surname;
    private String email;
    private String phoneNumber;

    public static GuestDTO fromReservationDTO(ReservationDTO dto) {
        return GuestDTO.builder()
                .name(dto.getName())
                .surname(dto.getSurname())
                .email(dto.getEmail())
                .phoneNumber(dto.getPhoneNumber())
                .build();
    }
}
