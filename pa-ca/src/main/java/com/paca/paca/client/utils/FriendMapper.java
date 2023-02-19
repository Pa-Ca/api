package com.paca.paca.client.utils;

import com.paca.paca.client.model.Friend;
import com.paca.paca.client.dto.FriendDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface FriendMapper {

    @Mapping(source = "requester.id", target = "requesterId")
    @Mapping(source = "addresser.id", target = "addresserId")
    FriendDTO toDTO(Friend friend);

    Friend toEntity(FriendDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "requester", ignore = true)
    @Mapping(target = "addresser", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Friend updateModel(FriendDTO dto, @MappingTarget Friend friend);
}
