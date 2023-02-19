package com.paca.paca.client.utils;

import org.mapstruct.*;

import com.paca.paca.client.model.Client;
import com.paca.paca.client.dto.ClientDTO;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    @Mapping(source = "user.id", target = "userId")
    ClientDTO toDTO(Client client);

    Client toEntity(ClientDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Client updateModel(ClientDTO dto, @MappingTarget Client client);
}
