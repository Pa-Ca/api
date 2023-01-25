package com.paca.paca.user;

import com.paca.paca.user.dto.UserDTO;
import com.paca.paca.user.dto.UserListDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.paca.paca.exception.exceptions.BadRequestException;
import com.paca.paca.exception.exceptions.NoContentException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseEntity<UserListDTO> getAll() {
        List<UserDTO> response = new ArrayList<>();
        userRepository.findAll().forEach(user -> {
            response.add (
                    UserDTO.builder()
                            .id(user.getId())
                            .email(user.getEmail())
                            .password(user.getPassword())
                            .verified(user.isVerified())
                            .loggedIn(user.isLoggedIn())
                            .role(user.getRoleId().getName().name())
                            .build()
            );
        });

        return ResponseEntity.ok(UserListDTO.builder().users(response).build());
    }

    public ResponseEntity<UserDTO> getById(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new NoContentException("User does not exists", 8)
        );
        UserDTO response = UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .verified(user.isVerified())
                .loggedIn(user.isLoggedIn())
                .role(user.getRoleId().getName().name())
                .build();
         return ResponseEntity.ok(response);
    }

    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NoContentException("User with email: " + email + " does not exists"));
    }

    public void save(UserDTO dto) {
        boolean userExists = userRepository.existsByEmail(dto.email);
        if (userExists)
            throw new BadRequestException("User already exists");

        if (dto.password.isEmpty())
            throw new BadRequestException("Password not found");

        userRepository.save(dto.toUser());
    }

    public void update(UserDTO dto) {
        Optional<User> current = userRepository.findById(dto.id);
        if (current.isEmpty())
            throw new BadRequestException("User does not exists");

        String email = dto.email;
        if (!current.get().getEmail().equals(email)) {
            boolean emailExists = userRepository.existsByEmail(dto.email);
            if (emailExists)
                throw new BadRequestException("This email is already taken");
        }

        userRepository.save(dto.toUser());
    }

    public void delete(Long id) {
        Optional<User> current = userRepository.findById(id);
        if (current.isEmpty())
            throw new BadRequestException("User does not exists");
        userRepository.deleteById(id);

    }

}
