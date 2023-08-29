package com.paca.paca.reservation.dto;

import lombok.*;

import java.util.List;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class BranchReservationsInfoDTO {

    List<ReservationInfoDTO> startedReservations;
    List<ReservationInfoDTO> acceptedReservations;
    List<ReservationInfoDTO> pendingReservations;
    List<ReservationInfoDTO> historicReservations;
    int currentHistoricPage;
    int totalHistoricPages;
    int totalHistoricElements;
}
