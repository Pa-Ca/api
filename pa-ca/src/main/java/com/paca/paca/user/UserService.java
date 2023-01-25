package com.paca.paca.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paca.paca.exception.exceptions.BadRequestException;
import com.paca.paca.exception.exceptions.ConflictException;
import com.paca.paca.exception.exceptions.NoContentException;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NoContentException(
                        "User with id: " + id + " does not exists",
                        8));
    }

    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NoContentException(
                        "User with email: " + email + " does not exists",
                        8));
    }

    public void save(UserDTO dto) {
        boolean userExists = userRepository.existsByEmail(dto.email);
        if (userExists) {
            throw new ConflictException("User already exists", 1);
        }

        if (dto.password.isEmpty()) {
            throw new BadRequestException("Password not found");
        }

        userRepository.save(dto.toUser());
    }

    public void update(UserDTO dto) {
        Optional<User> current = userRepository.findById(dto.id);
        if (current.isEmpty()) {
            throw new NoContentException("User does not exists", 8);
        }

        String email = dto.email;
        if (!current.get().getEmail().equals(email)) {
            boolean emailExists = userRepository.existsByEmail(dto.email);
            if (emailExists) {
                throw new ConflictException("This email is already taken", 10);
            }
        }

        userRepository.save(dto.toUser());
    }

    public void delete(Long id) {
        Optional<User> current = userRepository.findById(id);
        if (current.isEmpty()) {
            throw new NoContentException("User does not exists", 8);
        }
        userRepository.deleteById(id);

    }

}
