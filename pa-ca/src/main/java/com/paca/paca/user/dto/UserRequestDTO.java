package com.paca.paca.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDTO {
    public Long id;
    public String email;
    public String password;
    public Boolean verified;
    public Boolean loggedIn;
    public String role;
    public String provider;
    public String provider_id;
    public Integer reservationStatus;
}
