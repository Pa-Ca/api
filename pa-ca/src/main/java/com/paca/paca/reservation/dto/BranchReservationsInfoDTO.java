package com.paca.paca.reservation.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BranchReservationsInfoDTO {

    List<ReservationInfoDTO> startedReservations;
    List<ReservationInfoDTO> acceptedReservations;
    List<ReservationInfoDTO> pendingReservations;
    List<ReservationInfoDTO> historicReservations;
    int currentHistoricPage;
    int totalHistoricPages;
    int totalHistoricElements;
}
