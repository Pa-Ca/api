package com.paca.paca.user;

import org.springframework.stereotype.Service;

import com.paca.paca.exception.NoContentException;

import java.net.http.HttpResponse;
import java.util.List;

@Service
public class UserService {
    
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(
            () -> new NoContentException(
                "User with id: ${id} does not exists"
            )
        );
    }

}
