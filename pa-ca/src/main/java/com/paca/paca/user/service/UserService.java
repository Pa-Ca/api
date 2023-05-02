package com.paca.paca.user.service;

import com.paca.paca.user.model.User;
import com.paca.paca.user.dto.UserDTO;
import com.paca.paca.user.dto.UserListDTO;
import com.paca.paca.auth.utils.AuthUtils;
import com.paca.paca.user.utils.UserMapper;
import com.paca.paca.user.repository.UserRepository;
import com.paca.paca.exception.exceptions.ConflictException;
import com.paca.paca.exception.exceptions.NoContentException;
import com.paca.paca.exception.exceptions.BadRequestException;
import com.paca.paca.exception.exceptions.UnprocessableException;

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

@Service
public class UserService {

    private final UserMapper userMapper;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public UserListDTO getAll() {
        List<UserDTO> response = new ArrayList<>();
        userRepository.findAll().forEach(user -> response.add(
                UserDTO
                        .builder()
                        .id(user.getId())
                        .email(user.getEmail())
                        .password(user.getPassword())
                        .verified(user.getVerified())
                        .loggedIn(user.getLoggedIn())
                        .role(user.getRole().getName().name())
                        .build()));

        return UserListDTO.builder().users(response).build();
    }

    public UserDTO getById(Long id) throws NoContentException {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty())
            throw new NoContentException(
                    "User with id " + id + " does not exists",
                    12);
        else {
            UserDTO response = userMapper.toDTO(user.get());
            return response;
        }
    }

    public UserDTO update(Long id, UserDTO dto)
            throws NoContentException, UnprocessableException, ConflictException {
        Optional<User> current = userRepository.findById(id);
        if (current.isEmpty())
            throw new NoContentException(
                    "User with id " + id + " does not exists",
                    12);

        // Password validation
        if (dto.getPassword() != null) {
            AuthUtils.validatePassword(dto.getPassword());
            dto.setPassword(passwordEncoder.encode(dto.getPassword())); // Encode password before entity mapping
        }

        User user = userMapper.updateEntity(dto, current.get(), current.get().getRole().getName());

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
