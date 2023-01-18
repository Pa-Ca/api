package com.paca.paca.user;

import com.paca.paca.exception.BadRequestException;
import com.paca.paca.role.Role;
import com.paca.paca.statics.UserRole;

import java.util.Optional;

public class UserDTO {
    public Long id;
    public String email;
    public String password;
    public Boolean verified;
    public Boolean loggedIn;
    public String role;
}
