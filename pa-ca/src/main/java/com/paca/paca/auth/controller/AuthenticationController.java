package com.paca.paca.auth.controller;

import com.paca.paca.auth.dto.*;
import lombok.RequiredArgsConstructor;
import com.paca.paca.exception.exceptions.*;
import org.springframework.http.ResponseEntity;
import com.paca.paca.auth.service.AuthenticationService;
import com.paca.paca.auth.statics.AuthenticationStatics;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping(AuthenticationStatics.Endpoint.AUTH_PATH)
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping(AuthenticationStatics.Endpoint.SIGNUP)
    public ResponseEntity<LoginResponseDTO> signup(
            @RequestBody SignupRequestDTO request)
            throws BadRequestException, NoContentException, UnprocessableException, ConflictException {
        return ResponseEntity.ok(service.signup(request));
    }

    @PostMapping(AuthenticationStatics.Endpoint.LOGIN)
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request)
            throws BadRequestException, ForbiddenException {
        return ResponseEntity.ok(service.login(request));
    }

    @PostMapping(AuthenticationStatics.Endpoint.REFRESH)
    public ResponseEntity<RefreshResponseDTO> refresh(@RequestBody RefreshRequestDTO request)
            throws ForbiddenException {
        return ResponseEntity.ok(service.refresh(request));
    }

    @PostMapping(AuthenticationStatics.Endpoint.LOGOUT)
    public void logout(@RequestBody LogoutDTO request) throws BadRequestException {
        service.logout(request);
    }

    @PostMapping(AuthenticationStatics.Endpoint.RESET_PASSWORD_REQUEST)
    public ResponseEntity<ResetPasswordResponseDTO> resetPasswordRequest(@RequestBody ResetPasswordRequestDTO request)
            throws BadRequestException, NoContentException, IOException, MessagingException {
        return ResponseEntity.ok(service.resetPasswordRequest(request));
    }

    @PostMapping(AuthenticationStatics.Endpoint.RESET_PASSWORD + "/{token}")
    public void resetPassword(@RequestBody ResetPasswordDTO request, @PathVariable("token") String token)
            throws ForbiddenException, BadRequestException, UnprocessableException, NoContentException {
        service.resetPassword(request, token);
    }

    @PostMapping(AuthenticationStatics.Endpoint.VERIFY_EMAIL_REQUEST)
    public ResponseEntity<VerifyEmailResponseDTO> verifyEmailRequest(@RequestBody VerifyEmailRequestDTO request)
            throws BadRequestException, NoContentException, IOException, MessagingException {
        return ResponseEntity.ok(service.verifyEmailRequest(request));
    }

    @PostMapping(AuthenticationStatics.Endpoint.VERIFY_EMAIL + "/{token}")
    public void verifyEmail(@PathVariable("token") String token)
            throws ForbiddenException, BadRequestException, UnprocessableException, NoContentException {
        service.verifyEmail(token);
    }
}
