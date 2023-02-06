package com.paca.paca.user.utils;

import com.paca.paca.exception.exceptions.BadRequestException;
import com.paca.paca.statics.UserRole;
import com.paca.paca.user.dto.UserDTO;
import com.paca.paca.user.model.Role;
import com.paca.paca.user.model.User;
import org.mapstruct.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "role.name", target = "role")
    UserDTO toDTO(User user);

    @Mapping(source = "role", target = "role")
    @Mapping(target = "verified", defaultExpression = "java(false)")
    @Mapping(target = "loggedIn", defaultExpression = "java(false)")
    User toEntity(UserDTO dto, UserRole role);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "role", target = "role")
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "verified", ignore = true)
    @Mapping(target = "loggedIn", ignore = true)
    User updateEntity(UserDTO dto, @MappingTarget User user, UserRole role);

    default Role stringToRole (UserRole role) { return new Role(role); }
}


