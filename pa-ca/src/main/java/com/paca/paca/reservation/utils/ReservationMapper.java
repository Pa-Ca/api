package com.paca.paca.reservation.utils;

import org.mapstruct.*;
import com.paca.paca.branch.model.Branch;
import com.paca.paca.reservation.model.Guest;
import com.paca.paca.reservation.model.Invoice;
import com.paca.paca.reservation.model.Reservation;
import com.paca.paca.reservation.dto.ReservationDTO;

@Mapper(componentModel = "spring")
public interface ReservationMapper {

    @Mapping(source = "guest.id", target = "guestId")
    @Mapping(source = "branch.id", target = "branchId")
    @Mapping(source = "invoice.id", target = "invoiceId")
    ReservationDTO toDTO(Reservation reservation);

    @Mapping(target = "guest", ignore = true)
    @Mapping(target = "branch", ignore = true)
    @Mapping(target = "invoice", ignore = true)
    Reservation toEntity(ReservationDTO dto);

    default Reservation toEntity(ReservationDTO dto, Branch branch) {
        Reservation reservation = toEntity(dto);
        reservation.setBranch(branch);
        reservation.setGuest(null);
        return reservation;
    }

    default Reservation toEntity(ReservationDTO dto, Branch branch, Guest guest) {
        Reservation reservation = toEntity(dto);
        reservation.setBranch(branch);
        reservation.setGuest(guest);
        return reservation;
    }

    default Reservation toEntity(ReservationDTO dto, Branch branch, Invoice invoice) {
        Reservation reservation = toEntity(dto);
        reservation.setBranch(branch);
        reservation.setInvoice(invoice);
        return reservation;
    }

    default Reservation toEntity(ReservationDTO dto, Branch branch, Guest guest, Invoice invoice) {
        Reservation reservation = toEntity(dto);
        reservation.setBranch(branch);
        reservation.setGuest(guest);
        reservation.setInvoice(invoice);
        return reservation;
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "guest", ignore = true)
    @Mapping(target = "branch", ignore = true)
    @Mapping(target = "invoice", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Reservation updateModel(ReservationDTO dto, @MappingTarget Reservation reservation);
}
