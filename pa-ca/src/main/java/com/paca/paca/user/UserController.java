package com.paca.paca.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public ResponseEntity<List<UserDTO>> getAll() {
        return userService.getAll();
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable("id") Long id) {
        return userService.getById(id);
    }

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
