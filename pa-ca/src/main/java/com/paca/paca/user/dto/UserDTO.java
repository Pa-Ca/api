package com.paca.paca.user.dto;

import com.paca.paca.exception.exceptions.BadRequestException;
import com.paca.paca.user.model.Role;
import com.paca.paca.statics.UserRole;
import com.paca.paca.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    public Long id;
    public String email;
    public String password;
    public Boolean verified;
    public Boolean loggedIn;
    public String role;
}
