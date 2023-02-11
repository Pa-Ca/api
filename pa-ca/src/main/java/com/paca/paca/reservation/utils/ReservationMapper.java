package com.paca.paca.reservation.utils;

import com.paca.paca.reservation.model.Reservation;
import com.paca.paca.reservation.dto.ReservationDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ReservationMapper {
    ReservationDTO toDTO(Reservation reservation);

    Reservation toEntity(ReservationDTO dto);

    default Reservation updateModel(Reservation reservation, ReservationDTO dto) {
        if (dto.getRequestDate() != null) {
            reservation.setRequestDate(dto.getRequestDate());
        }
        if (dto.getReservationDate() != null) {
            reservation.setReservationDate(dto.getReservationDate());
        }
        if (dto.getClientNumber() != null) {
            reservation.setClientNumber(dto.getClientNumber());
        }
        if (dto.getPayment() != null) {
            reservation.setPayment(dto.getPayment());
        }
        if (dto.getStatus() != null) {
            reservation.setStatus(dto.getStatus());
        }
        if (dto.getPayDate() != null) {
            reservation.setPayDate(dto.getPayDate());
        }
        if (dto.getPrice() != null) {
            reservation.setPrice(dto.getPrice());
        }

        return reservation;
    }
}
