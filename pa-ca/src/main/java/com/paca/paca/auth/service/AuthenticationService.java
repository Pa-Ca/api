package com.paca.paca.auth.service;

import lombok.RequiredArgsConstructor;
import com.paca.paca.auth.utils.PassValidator;
import org.springframework.stereotype.Service;
import com.paca.paca.auth.utils.EmailValidator;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import com.paca.paca.user.model.Role;
import com.paca.paca.user.model.User;
import com.paca.paca.statics.UserRole;
import com.paca.paca.auth.dto.LoginRequestDTO;
import com.paca.paca.auth.dto.LoginResponseDTO;
import com.paca.paca.auth.dto.SignupRequestDTO;
import com.paca.paca.auth.dto.RefreshRequestDTO;
import com.paca.paca.auth.dto.RefreshResponseDTO;
import com.paca.paca.user.repository.RoleRepository;
import com.paca.paca.user.repository.UserRepository;

import com.paca.paca.exception.exceptions.ConflictException;
import com.paca.paca.exception.exceptions.ForbiddenException;
import com.paca.paca.exception.exceptions.NoContentException;
import com.paca.paca.exception.exceptions.BadRequestException;
import com.paca.paca.exception.exceptions.UnprocessableException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final UserDetailsService userDetailsService;

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
                .token(jwtService.generateToken(user, false))
                .refresh(jwtService.generateToken(user, true))
                .id(user.getId())
                .role(user.getRoleId().getName().name())
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
            throw new ForbiddenException("Authentication failed", 9);
        }

        User user = userRepository.findByEmail(request.getEmail()).orElseThrow();

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()));
        } catch (Exception e) {
            throw new ForbiddenException("Authentication failed", 9);
        }

        LoginResponseDTO response = LoginResponseDTO.builder()
                .token(jwtService.generateToken(user, false))
                .refresh(jwtService.generateToken(user, true))
                .id(user.getId())
                .role(user.getRoleId().getName().name())
                .build();
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<RefreshResponseDTO> refresh(RefreshRequestDTO request) throws ForbiddenException {
        String jwt = request.getRefresh();
        String userEmail;
        try {
            userEmail = jwtService.extractUsername(jwt);
        } catch (Exception e) {
            throw new ForbiddenException("Authentication failed1", 9);
        }
        if (userEmail != null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
            if (! jwtService.isTokenValid(jwt, userDetails) || ! jwtService.isTokenRefresh(jwt)) {
                throw new ForbiddenException("Authentication failed", 9);
            }
        }
        else {
            throw new ForbiddenException("Authentication failed", 9);
        }

        User user = userRepository.findByEmail(userEmail).orElseThrow();
        RefreshResponseDTO response = RefreshResponseDTO.builder()
                .token(jwtService.generateToken(user, false))
                .build();
        return ResponseEntity.ok(response);
    }
}
