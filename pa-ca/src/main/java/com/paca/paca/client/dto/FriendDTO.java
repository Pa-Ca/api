package com.paca.paca.client.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FriendDTO {

    private Long id;
    private Long requesterId;
    private Long addresserId;
    private Boolean accepted;
    private Boolean rejected;
}