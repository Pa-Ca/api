package com.paca.paca.client.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;

@Builder
@Getter
@Setter
public class FriendDTO {

    private Long id;
    private Long requesterId;
    private Long addresserId;
    private Boolean accepted;
    private Boolean rejected;
}