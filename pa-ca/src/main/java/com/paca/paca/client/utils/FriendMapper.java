package com.paca.paca.client.utils;

import com.paca.paca.client.model.Client;
import com.paca.paca.client.model.Friend;
import com.paca.paca.client.dto.FriendDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface FriendMapper {

    @Mapping(source = "requester.id", target = "requesterId")
    @Mapping(source = "addresser.id", target = "addresserId")
    FriendDTO toDTO(Friend friend);

    @Mapping(target = "requester", ignore = true)
    @Mapping(target = "addresser", ignore = true)
    Friend toEntity(FriendDTO dto);

    default Friend toEntity(FriendDTO dto, Client requester, Client addresser) {
        Friend friend = toEntity(dto);
        friend.setRequester(requester);
        friend.setAddresser(addresser);

        return friend;
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "requester", ignore = true)
    @Mapping(target = "addresser", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Friend updateModel(FriendDTO dto, @MappingTarget Friend friend);
}
