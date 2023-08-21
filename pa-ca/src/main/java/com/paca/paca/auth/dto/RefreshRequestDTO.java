package com.paca.paca.auth.dto;

import lombok.*;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class RefreshRequestDTO {

    private String refresh;
}
