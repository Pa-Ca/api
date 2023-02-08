package com.paca.paca.auth.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.paca.paca.auth.dto.LogoutDTO;
import com.paca.paca.auth.service.JwtService;
import com.paca.paca.auth.dto.LoginRequestDTO;
import com.paca.paca.auth.dto.LoginResponseDTO;
import com.paca.paca.auth.dto.SignupRequestDTO;
import com.paca.paca.auth.dto.RefreshRequestDTO;
import com.paca.paca.auth.dto.RefreshResponseDTO;
import com.paca.paca.auth.service.AuthenticationService;
import com.paca.paca.auth.statics.AuthenticationStatics;
import com.paca.paca.exception.exceptions.ConflictException;
import com.paca.paca.exception.exceptions.ForbiddenException;
import com.paca.paca.exception.exceptions.BadRequestException;
import com.paca.paca.exception.exceptions.UnprocessableException;

@CrossOrigin
@RestController
@RequestMapping(AuthenticationStatics.Endpoint.AUTH_PATH)
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    private final JwtService jwtService;

    @PostMapping(AuthenticationStatics.Endpoint.SIGNUP)
    public ResponseEntity<LoginResponseDTO> signup(
            @RequestBody SignupRequestDTO request)
            throws BadRequestException, UnprocessableException, ConflictException {
        return service.signup(request);
    }

    @PostMapping(AuthenticationStatics.Endpoint.LOGIN)
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request)
            throws ForbiddenException {
        return service.login(request);
    }

    @PostMapping(AuthenticationStatics.Endpoint.REFRESH)
    public ResponseEntity<RefreshResponseDTO> refresh(@RequestBody RefreshRequestDTO request)
            throws ForbiddenException {
        return service.refresh(request);
    }

    @PostMapping(AuthenticationStatics.Endpoint.LOGOUT)
    public void logout(@RequestBody LogoutDTO request) {
        jwtService.addTokenToBlackList(request.getRefresh());
        jwtService.addTokenToBlackList(request.getToken());
    }

}
