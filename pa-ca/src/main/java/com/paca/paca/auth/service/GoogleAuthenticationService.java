package com.paca.paca.auth.service;

import lombok.Getter;
import lombok.Setter;
import lombok.RequiredArgsConstructor;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;

import com.paca.paca.user.model.Role;
import com.paca.paca.user.model.User;
import com.paca.paca.statics.UserRole;
import com.paca.paca.auth.utils.AuthUtils;
import com.paca.paca.statics.AuthProvider;
import com.paca.paca.exception.exceptions.*;
import com.paca.paca.auth.dto.LoginResponseDTO;
import com.paca.paca.auth.dto.GoogleLoginRequestDTO;
import com.paca.paca.user.repository.RoleRepository;
import com.paca.paca.user.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Collections;

@Getter
@Setter
@Service
@RequiredArgsConstructor
public class GoogleAuthenticationService {

    private final JwtService jwtService;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationService authenticationService;

    @Value("${google.client.id}")
    private String CLIENT_ID;

    private Payload verifyToken(String token)
            throws BadRequestException {
        HttpTransport transport = new NetHttpTransport();
        JsonFactory jsonFactory = new GsonFactory();

        if (token == null) {
            throw new BadRequestException("No Google Access token was provided", 100);
        }

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                .setAudience(Collections.singletonList(CLIENT_ID))
                .build();

        // Try Google Token Verification
        GoogleIdToken idToken;
        try {
            idToken = verifier.verify(token);
        } catch (Exception e) {
            throw new BadRequestException("The Google Access token can't be verified", 102);
        }

        // Retrieve user data from access token
        return idToken.getPayload();
    }

    public LoginResponseDTO signup(GoogleLoginRequestDTO request)
            throws BadRequestException, UnprocessableException, ConflictException, ForbiddenException {
        Payload payload = verifyToken(request.getToken());

        // Email Validation
        if (userRepository.existsByEmail(payload.getEmail())) {
            throw new ConflictException("User already exists", 1);
        }

        // Role Validation
        AuthUtils.validateRole(request.getRole());
        Optional<Role> role = roleRepository.findByName(UserRole.valueOf(request.getRole()));
        if (role.isEmpty()) {
            throw new NotFoundException("Role " + request.getRole() + " does not exists");
        }

        // Create and save new User
        User user = User.builder()
                .email(payload.getEmail())
                .password(AuthUtils.randomPasswordGenerator(64))
                .role(role.get())
                .verified(false)
                .provider(AuthProvider.google.name())
                .providerId(payload.getSubject())
                .build();
        user = userRepository.save(user);

        // Generate JWT token and Response
        LoginResponseDTO response = LoginResponseDTO.builder()
                .token(jwtService.generateToken(user, JwtService.TokenType.TOKEN))
                .refresh(jwtService.generateToken(user, JwtService.TokenType.REFRESH))
                .id(user.getId())
                .role(user.getRole().getName().name())
                .build();

        return response;
    }

    public LoginResponseDTO login(GoogleLoginRequestDTO request)
            throws BadRequestException, UnprocessableException, ConflictException, ForbiddenException {
        Payload payload = verifyToken(request.getToken());

        if (!userRepository.existsByEmail(payload.getEmail())) {
            throw new ForbiddenException("Authentication failed", 9);
        }

        User user = userRepository.findByEmail(payload.getEmail()).get();
        LoginResponseDTO response = LoginResponseDTO.builder()
                .token(jwtService.generateToken(user, JwtService.TokenType.TOKEN))
                .refresh(jwtService.generateToken(user, JwtService.TokenType.REFRESH))
                .id(user.getId())
                .role(user.getRole().getName().name())
                .build();

        return response;
    }

}
