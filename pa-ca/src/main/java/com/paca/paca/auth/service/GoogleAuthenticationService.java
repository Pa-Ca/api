package com.paca.paca.auth.service;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.paca.paca.auth.dto.GoogleLoginRequestDTO;
import com.paca.paca.auth.dto.LoginRequestDTO;
import com.paca.paca.auth.dto.LoginResponseDTO;
import com.paca.paca.auth.dto.SignupRequestDTO;
import com.paca.paca.auth.utils.AuthUtils;
import com.paca.paca.exception.exceptions.BadRequestException;
import com.paca.paca.exception.exceptions.ConflictException;
import com.paca.paca.exception.exceptions.ForbiddenException;
import com.paca.paca.exception.exceptions.UnprocessableException;
import com.paca.paca.user.model.User;
import com.paca.paca.user.repository.UserRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;

import java.util.Collections;

@Service
@Getter
@Setter
@RequiredArgsConstructor
public class GoogleAuthenticationService {
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    @Value("${google.client.id}")
    private String CLIENT_ID;

    private Payload verifyToken(String token)
            throws BadRequestException {
        HttpTransport transport = new NetHttpTransport();
        JsonFactory jsonFactory = new JacksonFactory();

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
        SignupRequestDTO dto = SignupRequestDTO.builder()
                .email(payload.getEmail())
                .password(AuthUtils.randomPasswordGenerator(64))
                .role(request.getRole())
                .build();

        return authenticationService.signup(dto);
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
