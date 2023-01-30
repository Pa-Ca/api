package com.paca.paca.client.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class FriendRequestDTO {

    private Long id;
    private Long requesterId;
    private Long addresserId;
}