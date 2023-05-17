package com.paca.paca.auth.controller;

import com.paca.paca.auth.dto.LoginResponseDTO;
import com.paca.paca.auth.dto.GoogleLoginRequestDTO;
import com.paca.paca.auth.statics.AuthenticationStatics;
import com.paca.paca.exception.exceptions.ConflictException;
import com.paca.paca.exception.exceptions.ForbiddenException;
import com.paca.paca.exception.exceptions.BadRequestException;
import com.paca.paca.auth.service.GoogleAuthenticationService;
import com.paca.paca.exception.exceptions.UnprocessableException;

import lombok.RequiredArgsConstructor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping(AuthenticationStatics.Endpoint.AUTH_PATH)
@Tag(name = "02. Google Authentication", description = "Google Authentication Controller")
public class GoogleAuthenticationController {

    private final GoogleAuthenticationService service;

    @PostMapping(AuthenticationStatics.Endpoint.GOOGLE_SIGNUP)
    @Operation(summary = "User register", description = "Register a new user in the application using Google API")
    public ResponseEntity<LoginResponseDTO> googleSignup(@RequestBody GoogleLoginRequestDTO request)
            throws BadRequestException, UnprocessableException, ConflictException, ForbiddenException {
        return ResponseEntity.ok(service.signup(request));
    }

    @PostMapping(AuthenticationStatics.Endpoint.GOOGLE_LOGIN)
    @Operation(summary = "User login", description = "User login using Google API, which returns a token to use other application endpoints")
    public ResponseEntity<LoginResponseDTO> googleLogin(@RequestBody GoogleLoginRequestDTO request)
            throws BadRequestException, UnprocessableException, ConflictException, ForbiddenException {
        return ResponseEntity.ok(service.login(request));
    }
}
