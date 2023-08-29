package com.paca.paca.reservation.dto;

import lombok.*;

import java.util.List;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ReservationInfoListDTO {

    List<ReservationInfoDTO> reservations;
}
