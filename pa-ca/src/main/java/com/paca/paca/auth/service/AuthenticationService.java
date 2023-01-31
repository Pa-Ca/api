package com.paca.paca.auth.service;

import com.paca.paca.auth.utils.EmailValidator;
import com.paca.paca.auth.utils.PassValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import com.paca.paca.user.model.Role;
import com.paca.paca.user.model.User;
import com.paca.paca.user.repository.UserRepository;
import com.paca.paca.auth.config.JwtService;
import com.paca.paca.auth.dto.LoginRequestDTO;
import com.paca.paca.auth.dto.LoginResponseDTO;
import com.paca.paca.auth.dto.SignupRequestDTO;
import com.paca.paca.exception.exceptions.BadRequestException;
import com.paca.paca.exception.exceptions.ConflictException;
import com.paca.paca.exception.exceptions.ForbiddenException;
import com.paca.paca.exception.exceptions.NoContentException;
import com.paca.paca.exception.exceptions.UnprocessableException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public ResponseEntity<LoginResponseDTO> signup(SignupRequestDTO request)
            throws BadRequestException, UnprocessableException, ConflictException {
        // Email Validation
        String email = request.getEmail();
        EmailValidator.validate(email);
        if (repository.existsByEmail(email)) throw new ConflictException("User already exists", 1);

        // Password Validation
        String password = request.getPassword();
        PassValidator.validate(password);

        // Create and save new User
        var user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roleId(new Role(1L, "client"))
                .verified(false)
                .loggedIn(false)
                .build();
        repository.save(user);

        // Generate JWT token and Response
        LoginResponseDTO response = LoginResponseDTO.builder()
                .token(jwtService.generateToken(user))
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
        if (!repository.existsByEmail(email)) {
            throw new NoContentException("User does not exists", 8);
        }

        var user = repository.findByEmail(request.getEmail()).orElseThrow();
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
