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
import com.paca.paca.auth.utils.ValidateRolesInterceptor.ValidateRoles;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping(UserStatics.Endpoint.PATH)
public class UserController {

    private final UserService userService;

    @ValidateRoles({})
    @GetMapping(UserStatics.Endpoint.GET_ALL)
    public ResponseEntity<UserListDTO> getAll() {
        return new ResponseEntity<>(userService.getAll(), HttpStatus.OK);
    }

    @GetMapping(UserStatics.Endpoint.GET_BY_ID)
    public ResponseEntity<UserResponseDTO> getById(@PathVariable("id") Long id) throws BadRequestException {
        return new ResponseEntity<>(userService.getById(id), HttpStatus.OK);
    }

    @ValidateUser
    @PatchMapping(UserStatics.Endpoint.UPDATE)
    public ResponseEntity<UserResponseDTO> update(@PathVariable("id") Long id, @RequestBody UserRequestDTO user)
            throws BadRequestException, UnprocessableException, ConflictException {
        return new ResponseEntity<>(userService.update(id, user), HttpStatus.OK);
    }

    @ValidateUser
    @DeleteMapping(UserStatics.Endpoint.DELETE)
    public void delete(@PathVariable("id") Long id) throws BadRequestException {
        userService.delete(id);
    }
}
