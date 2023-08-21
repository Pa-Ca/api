package com.paca.paca.reservation.dto;

import lombok.*;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class GuestInfoDTO {

    private GuestDTO guest;
    private Long clientGuestId;
}