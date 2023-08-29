package com.paca.paca.auth.dto;

import lombok.*;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordDTO {

    private String email;
    private String oldPassword;
    private String newPassword;
}
