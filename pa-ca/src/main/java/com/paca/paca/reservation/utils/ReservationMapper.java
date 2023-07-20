package com.paca.paca.reservation.utils;

import org.mapstruct.*;
import com.paca.paca.branch.model.Branch;
import com.paca.paca.reservation.model.Guest;
import com.paca.paca.reservation.model.Reservation;
import com.paca.paca.reservation.dto.ReservationDTO;

@Mapper(componentModel = "spring")
public interface ReservationMapper {

    @Mapping(target = "name", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "surname", ignore = true)
    @Mapping(target = "haveGuest", ignore = true)
    @Mapping(target = "phoneNumber", ignore = true)
    @Mapping(target = "identityDocument", ignore = true)
    @Mapping(source = "guest.id", target = "guestId")
    @Mapping(source = "branch.id", target = "branchId")
    ReservationDTO toDTO(Reservation reservation);

    @Mapping(target = "branch", ignore = true)
    @Mapping(target = "guest", ignore = true)
    Reservation toEntity(ReservationDTO dto);

    default Reservation toEntity(ReservationDTO dto, Branch branch, Guest guest) {
        Reservation reservation = toEntity(dto);
        reservation.setBranch(branch);
        reservation.setGuest(guest);
        return reservation;
    }

    default Reservation toEntity(ReservationDTO dto, Branch branch) {
        Reservation reservation = toEntity(dto);
        reservation.setBranch(branch);
        reservation.setGuest(null);
        return reservation;
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "guest", ignore = true)
    @Mapping(target = "branch", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Reservation updateModel(ReservationDTO dto, @MappingTarget Reservation reservation);
}
