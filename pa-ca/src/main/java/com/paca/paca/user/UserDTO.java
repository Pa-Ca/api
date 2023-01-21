package com.paca.paca.user;

import com.paca.paca.exception.exceptions.BadRequestException;
import com.paca.paca.role.Role;
import com.paca.paca.statics.UserRole;

public class UserDTO {

    public Long id;
    public String email;
    public String password;
    public Boolean verified;
    public Boolean loggedIn;
    public String role;

    public User toUser() {
        if (this.role.isEmpty())
            throw new BadRequestException("The role attribute not found");

        UserRole role;
        try {
            role = UserRole.valueOf(this.role);
        } catch (Exception e) {
            throw new BadRequestException("The role given is not valid");
        }

        return new User(
                this.id,
                this.email,
                this.password,
                this.verified != null && this.verified,
                this.loggedIn != null && this.loggedIn,
                new Role((long) UserRole.valueOf(this.role).ordinal(), role));
    }
}
