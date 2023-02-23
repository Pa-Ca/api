package com.paca.paca.reservation.utils;

import org.mapstruct.*;
import com.paca.paca.branch.model.Branch;
import com.paca.paca.reservation.model.Reservation;
import com.paca.paca.reservation.dto.ReservationDTO;

@Mapper(componentModel = "spring")
public interface ReservationMapper {

    @Mapping(source = "branch.id", target = "branchId")
    ReservationDTO toDTO(Reservation reservation);

    @Mapping(target = "branch", ignore = true)
    Reservation toEntity(ReservationDTO dto);

    default Reservation toEntity(ReservationDTO dto, Branch branch) {
        Reservation reservation = toEntity(dto);
        reservation.setBranch(branch);
        return reservation;
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "branch", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Reservation updateModel(ReservationDTO dto, @MappingTarget Reservation reservation);
}

