package com.paca.paca.auth.service;

import lombok.RequiredArgsConstructor;
import com.paca.paca.auth.utils.AuthUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import com.paca.paca.user.model.Role;
import com.paca.paca.user.model.User;
import com.paca.paca.statics.UserRole;
import com.paca.paca.auth.dto.LogoutDTO;
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

    public LoginResponseDTO signup(SignupRequestDTO request)
            throws BadRequestException, UnprocessableException, ConflictException {
        // Email Validation
        String email = request.getEmail();
        AuthUtils.validateEmail(email);
        if (userRepository.existsByEmail(email)) {
            throw new ConflictException("User already exists", 1);
        }

        // Password Validation
        String password = request.getPassword();
        AuthUtils.validatePassword(password);

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
                .role(role.get())
                .verified(false)
                .loggedIn(false)
                .build();
        user = userRepository.save(user);

        // Generate JWT token and Response
        LoginResponseDTO response = LoginResponseDTO.builder()
                .token(jwtService.generateToken(user, false))
                .refresh(jwtService.generateToken(user, true))
                .id(user.getId())
                .role(user.getRole().getName().name())
                .build();

        return response;
    }

    public LoginResponseDTO login(LoginRequestDTO request)
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
                .role(user.getRole().getName().name())
                .build();
        return response;
    }

    public RefreshResponseDTO refresh(RefreshRequestDTO request) throws ForbiddenException {
        String jwt = request.getRefresh();
        String userEmail;

        try {
            userEmail = jwtService.extractEmail(jwt);
        } catch (Exception e) {
            throw new ForbiddenException("Authentication failed", 9);
        }
        if (userEmail != null) {
            Optional<User> user = userRepository.findByEmail(userEmail);
            if (user.isEmpty()) {
                throw new ForbiddenException("Authentication failed", 9);
            }
            if (!jwtService.isTokenValid(jwt, user.get()) || !jwtService.isTokenRefresh(jwt)) {
                throw new ForbiddenException("Authentication failed", 9);
            }
        } else {
            throw new ForbiddenException("Authentication failed", 9);
        }

        User user = userRepository.findByEmail(userEmail).get();
        RefreshResponseDTO response = RefreshResponseDTO.builder()
                .token(jwtService.generateToken(user, false))
                .build();
        return response;
    }

    public void logout(LogoutDTO request) throws BadRequestException {
        String refresh = request.getRefresh();
        String token = request.getToken();

        if (refresh == null) {
            throw new BadRequestException("Refresh not found");
        }
        if (token == null) {
            throw new BadRequestException("Token not found");
        }

        jwtService.addTokenToBlackList(refresh);
        jwtService.addTokenToBlackList(token);
    }
}
