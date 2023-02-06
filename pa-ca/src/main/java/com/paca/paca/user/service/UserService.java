package com.paca.paca.user.service;

import com.paca.paca.auth.utils.EmailValidator;
import com.paca.paca.auth.utils.PassValidator;
import com.paca.paca.exception.exceptions.ConflictException;
import com.paca.paca.exception.exceptions.UnprocessableException;
import com.paca.paca.statics.UserRole;
import com.paca.paca.user.dto.UserDTO;
import com.paca.paca.user.repository.UserRepository;
import com.paca.paca.user.dto.UserListDTO;
import com.paca.paca.user.model.User;
import com.paca.paca.user.utils.UserMapper;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.paca.paca.exception.exceptions.BadRequestException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserMapper userMapper;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private void validateRole(String role) throws BadRequestException {
        if (role.isEmpty())
            throw new BadRequestException("The role attribute not found");

        try {
            UserRole.valueOf(role);
        } catch (Exception e) {
            throw new BadRequestException("The role given is not valid");
        }
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
                .role(user.getRole().getName().name())
                .build()
        ));

        return ResponseEntity.ok(UserListDTO.builder().users(response).build());
    }

    public UserListDTO getAll() {
        List<UserDTO> response = new ArrayList<>();
        userRepository.findAll().forEach(user -> response.add (
            UserDTO
                .builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .verified(user.getVerified())
                .loggedIn(user.getLoggedIn())
                .role(user.getRole().getName().name())
                .build()
        ));


        return UserListDTO.builder().users(response).build();
    }

    public UserDTO getById(Long id) throws BadRequestException {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) throw new BadRequestException("User does not exists");
        else {
            UserDTO response = userMapper.toDTO(user.get());
            return response;
        }
    }

    public UserDTO update(Long id, UserDTO dto)

            throws BadRequestException, UnprocessableException, ConflictException {
        Optional<User> current = userRepository.findById(id);
        if (current.isEmpty())
            throw new BadRequestException("User does not exists");

        // Email Validation
        if (dto.getEmail() != null) {
            EmailValidator.validate(dto.email);
            if (userRepository.existsByEmail(dto.getEmail()))
                throw new BadRequestException("This email is already taken");
        }

        // Password validation
        if (dto.getPassword() != null) {
            PassValidator.validate(dto.getPassword());
            dto.setPassword(passwordEncoder.encode(dto.getPassword())); // Encode password before entity mapping
        }

        // Role validation
        if (dto.getRole() != null)
            validateRole(dto.getRole());

        User user = userMapper.updateEntity(dto, current.get(), UserRole.valueOf(
                (dto.role != null) ? dto.role : current.get().getRole().getName().name()
        ));

        userRepository.save(user);
        return userMapper.toDTO(user);
    }

    public void delete(Long id) throws BadRequestException {
        Optional<User> current = userRepository.findById(id);
        if (current.isEmpty())
            throw new BadRequestException("User does not exists");
        userRepository.deleteById(id);
    }

}
