package com.paca.paca.user;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    
    @GetMapping
    public List<User> getAll() {
        return List.of (
            new User (
                1L, 
                "sivira@carlos.com", 
                "1234"
            )
        );
    }
}
