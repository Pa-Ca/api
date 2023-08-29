package com.paca.paca.auth.dto;

import lombok.*;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class LogoutDTO {

    private String token;
    private String refresh;
}
