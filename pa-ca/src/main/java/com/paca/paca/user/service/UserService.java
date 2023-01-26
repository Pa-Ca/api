package com.paca.paca.user.service;

import com.paca.paca.statics.UserRole;
import com.paca.paca.user.dto.UserDTO;
import com.paca.paca.user.repository.UserRepository;
import com.paca.paca.user.dto.UserListDTO;
import com.paca.paca.user.model.User;
import com.paca.paca.user.utils.UserMapper;
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
    private final UserMapper userMapper;

    private void validateRole(UserDTO dto) {
        if (dto.role.isEmpty()) throw new BadRequestException("The role attribute not found");
        UserRole role;
        try {
            role = UserRole.valueOf(dto.role);
        } catch (Exception e) {
            throw new BadRequestException("The role given is not valid");
        }

        userRepository.save(userMapper.toEntity(dto, role));
    }

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public ResponseEntity<UserListDTO> getAll() {
        List<UserDTO> response = new ArrayList<>();
        userRepository.findAll().forEach(user -> response.add (
            UserDTO
                .builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .verified(user.getVerified())
                .loggedIn(user.getLoggedIn())
                .role(user.getRoleId().getName().name())
                .build()
        ));

        return ResponseEntity.ok(UserListDTO.builder().users(response).build());
    }

    public ResponseEntity<UserDTO> getById(Long id) throws NoContentException {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) throw new NoContentException("User with id " + id + " does not exists", 8);
        else return ResponseEntity.ok(userMapper.toDTO(user.get()));
    }

    public ResponseEntity<UserDTO> getByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) throw new NoContentException("User with email: " + email + " does not exists");
        else return ResponseEntity.ok(userMapper.toDTO(user.get()));
    }

    public void save(UserDTO dto) {
        boolean userExists = userRepository.existsByEmail(dto.email);
        if (userExists)
            throw new BadRequestException("User already exists");

        if (dto.password.isEmpty())
            throw new BadRequestException("Password not found");

        validateRole(dto);
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

        validateRole(dto);
    }

    public void delete(Long id) {
        Optional<User> current = userRepository.findById(id);
        if (current.isEmpty())
            throw new BadRequestException("User does not exists");
        userRepository.deleteById(id);

    }

}
