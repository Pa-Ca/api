package com.paca.paca.reservation.utils;

import org.mapstruct.*;
import com.paca.paca.reservation.model.Guest;
import com.paca.paca.reservation.dto.GuestDTO;

@Mapper(componentModel = "spring")
public interface GuestMapper {

    GuestDTO toDTO(Guest guest);

    Guest toEntity(GuestDTO dto);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Guest updateModel(GuestDTO dto, @MappingTarget Guest guest);
}
