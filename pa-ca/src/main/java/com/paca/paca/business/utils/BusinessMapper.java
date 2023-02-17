package com.paca.paca.business.utils;

import com.paca.paca.business.model.Business;
import com.paca.paca.business.dto.BusinessDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface BusinessMapper {
    BusinessDTO toDTO(Business business);

    Business toEntity(BusinessDTO dto);

    default Business updateModel(Business client, BusinessDTO dto) {
        if (dto.getName() != null) {
            client.setName(dto.getName());
        }
        return client;
    }
}