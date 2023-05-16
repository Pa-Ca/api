package com.paca.paca.user.controller;

import com.paca.paca.user.dto.UserRequestDTO;
import com.paca.paca.user.dto.UserListDTO;
import com.paca.paca.user.dto.UserResponseDTO;
import com.paca.paca.user.service.UserService;
import com.paca.paca.user.statics.UserStatics;
import com.paca.paca.exception.exceptions.ConflictException;
import com.paca.paca.exception.exceptions.BadRequestException;
import com.paca.paca.exception.exceptions.UnprocessableException;
import com.paca.paca.user.utils.ValidateUserInterceptor.ValidateUser;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import com.paca.paca.auth.utils.ValidateRolesInterceptor.ValidateRoles;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(UserStatics.Endpoint.PATH)
@Tag(name = "02. User", description = "User Management Controller")
public class UserController {

    private final UserService userService;

    @ValidateRoles({})
    @GetMapping(UserStatics.Endpoint.GET_ALL)
    @Operation(summary = "Get all users", description = "Returns a list with all users")
    public ResponseEntity<UserListDTO> getAll() {
        return new ResponseEntity<>(userService.getAll(), HttpStatus.OK);
    }

    @GetMapping(UserStatics.Endpoint.GET_BY_ID)
    @Operation(summary = "Get user by ID", description = "Gets the data of a user given its ID")
    public ResponseEntity<UserResponseDTO> getById(@PathVariable("id") Long id) throws BadRequestException {
        return new ResponseEntity<>(userService.getById(id), HttpStatus.OK);
    }

    @ValidateUser
    @PatchMapping(UserStatics.Endpoint.UPDATE)
    @Operation(summary = "Update user", description = "Updates the data of a user given its ID")
    public ResponseEntity<UserResponseDTO> update(@PathVariable("id") Long id, @RequestBody UserRequestDTO user)
            throws BadRequestException, UnprocessableException, ConflictException {
        return new ResponseEntity<>(userService.update(id, user), HttpStatus.OK);
    }

    @ValidateUser
    @DeleteMapping(UserStatics.Endpoint.DELETE)
    @Operation(summary = "Delete user", description = "Delete the data of a user given its ID")
    public void delete(@PathVariable("id") Long id) throws BadRequestException {
        userService.delete(id);
    }
}
