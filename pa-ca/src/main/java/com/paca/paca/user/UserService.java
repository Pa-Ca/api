package com.paca.paca.user;

import com.paca.paca.exception.BadRequestException;
import com.paca.paca.role.Role;
import com.paca.paca.role.RoleRepository;
import com.paca.paca.statics.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.BeanDefinitionDsl;
import org.springframework.stereotype.Service;

import com.paca.paca.exception.NoContentException;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    
    private final UserRepository userRepository;
    @Autowired private RoleRepository roleRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User getById(Long id) {
        return userRepository.findById(id)
            .orElseThrow( () -> new NoContentException( "User with id: " + id + " does not exists")
        );
    }

    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow( () -> new NoContentException("User with email: " + email + " does not exists")
        );
    }

    public void save(UserDTO dto) {
        boolean userExists = userRepository.existsByEmail(dto.email);
        if (userExists) throw new BadRequestException("User already exists");

        if (dto.password.isEmpty()) throw new BadRequestException("Password not found");

        userRepository.save(dto.toUser());
    }

    public void update(UserDTO dto) {
        Optional<User> current = userRepository.findById(dto.id);
        if (current.isEmpty()) throw new BadRequestException("User does not exists");

        String email = dto.email;
        if (!current.get().getEmail().equals(email)) {
            boolean emailExists = userRepository.existsByEmail(dto.email);
            if (emailExists) throw new BadRequestException("This email is already taken");
        }

        userRepository.save(dto.toUser());
    }

    public void delete(Long id) {
        Optional<User> current = userRepository.findById(id);
        if (current.isEmpty()) throw new BadRequestException("User does not exists");
        userRepository.deleteById(id);

    }

}
