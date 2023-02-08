package com.paca.paca.client.utils;

import com.paca.paca.client.model.Client;
import com.paca.paca.client.dto.ClientDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ClientMapper {
    ClientDTO toDTO(Client client);

    Client toEntity(ClientDTO dto);

    default Client updateModel(Client client, ClientDTO dto) {
        if (dto.getName() != null) {
            client.setName(dto.getName());
        }
        if (dto.getSurname() != null) {
            client.setSurname(dto.getSurname());
        }
        if (dto.getStripeCustomerId() != null) {
            client.setStripeCustomerId(dto.getStripeCustomerId());
        }
        if (dto.getPhoneNumber() != null) {
            client.setPhoneNumber(dto.getPhoneNumber());
        }
        if (dto.getAddress() != null) {
            client.setAddress(dto.getAddress());
        }
        if (dto.getDateOfBirth() != null) {
            client.setDateOfBirth(dto.getDateOfBirth());
        }

        return client;
    }
}
