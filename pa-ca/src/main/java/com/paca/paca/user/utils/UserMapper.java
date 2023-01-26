package com.paca.paca.user.utils;

import com.paca.paca.exception.exceptions.BadRequestException;
import com.paca.paca.statics.UserRole;
import com.paca.paca.user.dto.UserDTO;
import com.paca.paca.user.model.Role;
import com.paca.paca.user.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(source = "roleId.name", target = "role")
    UserDTO toDTO(User user);
    @Mapping(source = "role", target = "roleId")
    @Mapping(target = "verified", defaultExpression = "java(false)")
    @Mapping(target = "loggedIn", defaultExpression = "java(false)")
    User toEntity(UserDTO dto, UserRole role);

    default Role stringToRole (UserRole role) { return new Role(role); }
}


