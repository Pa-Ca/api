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
    List<ReservationDTO> startedReservations;
    List<ReservationDTO> acceptedReservations;
    List<ReservationDTO> pendingReservations;
    List<ReservationDTO> historicReservations;
    int currentHistoricPage;
    int totalHistoricPages;
}
