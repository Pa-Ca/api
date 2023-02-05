package com.paca.paca.user.controller;

import com.paca.paca.exception.exceptions.BadRequestException;
import com.paca.paca.exception.exceptions.ConflictException;
import com.paca.paca.exception.exceptions.UnprocessableException;
import com.paca.paca.user.dto.UserDTO;
import com.paca.paca.user.dto.UserListDTO;
import com.paca.paca.user.model.User;
import com.paca.paca.user.service.UserService;
import com.paca.paca.user.statics.UserStatics;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping(UserStatics.Endpoint.USER_PATH)
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<UserListDTO> getAll() {
        return userService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getById(@PathVariable("id") Long id) throws BadRequestException {
        return userService.getById(id);
    }

    @GetMapping("/2/{id}")
    public ResponseEntity<User> getById2(@PathVariable("id") Long id) throws BadRequestException {
        return userService.getById2(id);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserDTO> update(@PathVariable("id") Long id, @RequestBody UserDTO user)
            throws BadRequestException, UnprocessableException, ConflictException {
        return userService.update(id, user);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) throws BadRequestException {
        userService.delete(id);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserDTO> getByEmail(@PathVariable("email") String email) throws BadRequestException {
        return userService.getByEmail(email);
    }
}
