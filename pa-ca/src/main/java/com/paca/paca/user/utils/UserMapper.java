package com.paca.paca.user.utils;

import com.paca.paca.statics.UserRole;
import com.paca.paca.user.dto.UserRequestDTO;
import com.paca.paca.user.dto.UserResponseDTO;
import com.paca.paca.user.model.Role;
import com.paca.paca.user.model.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "role.name", target = "role")
    UserResponseDTO toDTO(User user);

    @Mapping(source = "role", target = "role")
    @Mapping(target = "verified", defaultExpression = "java(false)")
    @Mapping(target = "loggedIn", defaultExpression = "java(false)")
    User toEntity(UserRequestDTO dto, UserRole role);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    User updateEntity(UserRequestDTO dto, @MappingTarget User user, UserRole role);

    default Role stringToRole(UserRole role) {
        return new Role(role);
    }
}
