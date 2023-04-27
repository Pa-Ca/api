package com.paca.paca.user.controller;

import com.paca.paca.user.dto.UserDTO;
import com.paca.paca.user.dto.UserListDTO;
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
@RequestMapping(UserStatics.Endpoint.USER_PATH)
public class UserController {

    private final UserService userService;

    @GetMapping
    @ValidateRoles({})
    public ResponseEntity<UserListDTO> getAll() {
        return new ResponseEntity<>(userService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getById(@PathVariable("id") Long id) throws BadRequestException {
        return new ResponseEntity<>(userService.getById(id), HttpStatus.OK);
    }

    @ValidateUser
    @PatchMapping("/{id}")
    public ResponseEntity<UserDTO> update(@PathVariable("id") Long id, @RequestBody UserDTO user)
            throws BadRequestException, UnprocessableException, ConflictException {
        return new ResponseEntity<>(userService.update(id, user), HttpStatus.OK);
    }

    @ValidateUser
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) throws BadRequestException {
        userService.delete(id);
    }
}
