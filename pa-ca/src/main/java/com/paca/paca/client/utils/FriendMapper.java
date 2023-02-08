package com.paca.paca.client.utils;

import com.paca.paca.client.model.Friend;
import com.paca.paca.client.dto.FriendDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface FriendMapper {
    FriendDTO toDTO(Friend friend);

    Friend toEntity(FriendDTO dto);

    default Friend updateModel(Friend friend, FriendDTO dto) {
        if (dto.getAccepted() != null) {
            friend.setAccepted(dto.getAccepted());
        }
        if (dto.getRejected() != null) {
            friend.setRejected(dto.getRejected());
        }

        return friend;
    }
}
