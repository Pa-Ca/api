package com.paca.paca.auth.controller;

import com.paca.paca.auth.dto.GoogleLoginRequestDTO;
import com.paca.paca.auth.dto.LoginResponseDTO;
import com.paca.paca.auth.service.GoogleAuthenticationService;
import com.paca.paca.auth.statics.AuthenticationStatics;
import com.paca.paca.exception.exceptions.BadRequestException;
import com.paca.paca.exception.exceptions.ConflictException;
import com.paca.paca.exception.exceptions.ForbiddenException;
import com.paca.paca.exception.exceptions.UnprocessableException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping(AuthenticationStatics.Endpoint.AUTH_PATH)
@RequiredArgsConstructor
public class GoogleAuthenticationController {

    private final GoogleAuthenticationService service;

    @PostMapping(AuthenticationStatics.Endpoint.GOOGLE_SIGNUP)
    public ResponseEntity<LoginResponseDTO> googleSignup(@RequestBody GoogleLoginRequestDTO request)
            throws BadRequestException, UnprocessableException, ConflictException, ForbiddenException {
        return ResponseEntity.ok(service.signup(request));
    }

    @PostMapping(AuthenticationStatics.Endpoint.GOOGLE_LOGIN)
    public ResponseEntity<LoginResponseDTO> googleLogin(@RequestBody GoogleLoginRequestDTO request)
            throws BadRequestException, UnprocessableException, ConflictException, ForbiddenException {
        return ResponseEntity.ok(service.login(request));
    }
}
