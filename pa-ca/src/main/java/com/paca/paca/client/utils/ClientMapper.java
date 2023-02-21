package com.paca.paca.client.utils;

import org.mapstruct.*;

import com.paca.paca.user.model.User;
import com.paca.paca.client.model.Client;
import com.paca.paca.client.dto.ClientDTO;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.email", target = "email")
    ClientDTO toDTO(Client client);

    @Mapping(target = "user", ignore = true)
    Client toEntity(ClientDTO dto);

    default Client toEntity(ClientDTO dto, User user) {
        Client client = toEntity(dto);
        client.setUser(user);
        return client;
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Client updateModel(ClientDTO dto, @MappingTarget Client client);

}
