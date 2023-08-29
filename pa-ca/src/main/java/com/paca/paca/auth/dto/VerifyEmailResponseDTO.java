package com.paca.paca.auth.dto;

import lombok.*;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class VerifyEmailResponseDTO {

    private String token;
}
