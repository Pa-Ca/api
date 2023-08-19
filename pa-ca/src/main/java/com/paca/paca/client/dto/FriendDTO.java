package com.paca.paca.client.dto;

import lombok.*;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class FriendDTO {

    private Long id;
    private Long requesterId;
    private Long addresserId;
    private Boolean accepted;
    private Boolean rejected;
}