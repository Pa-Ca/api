package com.paca.paca.auth.service;

import com.paca.paca.auth.utils.EmailValidator;
import com.paca.paca.auth.utils.PassValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import com.paca.paca.user.model.Role;
import com.paca.paca.user.model.User;
import com.paca.paca.statics.UserRole;
import com.paca.paca.auth.config.JwtService;
import com.paca.paca.auth.dto.LoginRequestDTO;
import com.paca.paca.auth.dto.LoginResponseDTO;
import com.paca.paca.auth.dto.SignupRequestDTO;
import com.paca.paca.user.repository.RoleRepository;
import com.paca.paca.user.repository.UserRepository;
import com.paca.paca.exception.exceptions.BadRequestException;
import com.paca.paca.exception.exceptions.ConflictException;
import com.paca.paca.exception.exceptions.ForbiddenException;
import com.paca.paca.exception.exceptions.NoContentException;
import com.paca.paca.exception.exceptions.UnprocessableException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    private void validateRole(String role) throws BadRequestException {
        if (role.isEmpty())
            throw new BadRequestException("The role attribute not found");

        try {
            UserRole.valueOf(role);
        } catch (Exception e) {
            throw new BadRequestException("The role given is not valid");
        }
    }

    public ResponseEntity<LoginResponseDTO> signup(SignupRequestDTO request)
            throws BadRequestException, UnprocessableException, ConflictException {
        // Email Validation
        String email = request.getEmail();
        EmailValidator.validate(email);
        if (userRepository.existsByEmail(email)) {
            throw new ConflictException("User already exists", 1);
        }

        // Password Validation
        String password = request.getPassword();
        PassValidator.validate(password);

        // Role Validation
        validateRole(request.getRole());
        Optional<Role> role = roleRepository.findByName(UserRole.valueOf(request.getRole()));
        if (role.isEmpty()) {
            throw new NoContentException("Role " + request.getRole() + " does not exists");
        }

        // Create and save new User
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roleId(role.get())
                .verified(false)
                .loggedIn(false)
                .build();
        userRepository.save(user);

        // Generate JWT token and Response
        LoginResponseDTO response = LoginResponseDTO.builder()
                .token(jwtService.generateToken(user))
                .id(user.getId())
                .build();

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<LoginResponseDTO> login(LoginRequestDTO request)
            throws BadRequestException, NoContentException, ForbiddenException {
        String email = request.getEmail();
        String password = request.getPassword();

        if (email == null) {
            throw new BadRequestException("Email not found");
        }
        if (password == null) {
            throw new BadRequestException("Password not found");
        }
        if (!userRepository.existsByEmail(email)) {
            throw new NoContentException("User does not exists", 8);
        }

        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        if (password.equals(user.getPassword())) {
            throw new ForbiddenException("Authentication failed", 9);
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()));
        var jwtToken = jwtService.generateToken(user);
        return ResponseEntity.ok(LoginResponseDTO.builder()
                .token(jwtToken)
                .build());
    }
}
