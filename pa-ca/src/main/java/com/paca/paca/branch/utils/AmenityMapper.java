package com.paca.paca.branch.utils;

import com.paca.paca.branch.model.Amenity;
import com.paca.paca.branch.dto.AmenityDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface AmenityMapper {
    AmenityDTO toDTO(Amenity amenity);
    Amenity toEntity(AmenityDTO dto);
}


