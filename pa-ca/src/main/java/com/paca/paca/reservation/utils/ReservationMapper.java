package com.paca.paca.reservation.utils;

import com.paca.paca.reservation.model.Reservation;
import com.paca.paca.reservation.dto.ReservationDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ReservationMapper {

    @Mapping(source = "branch.id", target = "branchId")
    ReservationDTO toDTO(Reservation reservation);

    Reservation toEntity(ReservationDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "branch", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Reservation updateModel(ReservationDTO dto, @MappingTarget Reservation reservation);
}
