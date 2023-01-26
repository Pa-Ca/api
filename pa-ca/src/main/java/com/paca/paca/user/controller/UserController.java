package com.paca.paca.user.controller;

import com.paca.paca.user.dto.UserDTO;
import com.paca.paca.user.dto.UserListDTO;
import com.paca.paca.user.model.User;
import com.paca.paca.user.service.UserService;
import com.paca.paca.user.statics.UserStatics;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping(UserStatics.Endpoint.USER_PATH)
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<UserListDTO> getAll() { return userService.getAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getById(@PathVariable("id") Long id) { return userService.getById(id); }

    @GetMapping("/email/{email}")
    public User getByEmail(@PathVariable("email") String email) {
        return userService.getByEmail(email);
    }

    @PostMapping("/")
    public void save(@RequestBody UserDTO user) {
        userService.save(user);
    }

    @PutMapping("/")
    public void update(@RequestBody UserDTO user) {
        userService.update(user);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        userService.delete(id);
    }
}
