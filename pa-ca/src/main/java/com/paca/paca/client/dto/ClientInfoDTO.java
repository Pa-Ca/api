package com.paca.paca.client.dto;

import lombok.*;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ClientInfoDTO {

    private ClientDTO client;
    private Long clientGuestId;
}
