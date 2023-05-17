package com.paca.paca.auth.controller;

import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

import com.paca.paca.auth.dto.*;
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
@RequiredArgsConstructor
@RequestMapping(AuthenticationStatics.Endpoint.AUTH_PATH)
@Tag(name = "01. Authentication", description = "User Access Management and Authentication Controller")
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping(AuthenticationStatics.Endpoint.SIGNUP)
    @Operation(summary = "User register", description = "Register a new user in the application")
    public ResponseEntity<LoginResponseDTO> signup(
            @RequestBody SignupRequestDTO request)
            throws BadRequestException, NoContentException, UnprocessableException, ConflictException {
        return ResponseEntity.ok(service.signup(request));
    }

    @PostMapping(AuthenticationStatics.Endpoint.LOGIN)
    @Operation(summary = "User login", description = "User login, which returns a token to use other application endpoints")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request)
            throws BadRequestException, ForbiddenException {
        return ResponseEntity.ok(service.login(request));
    }

    @Operation(summary = "Refresh token operation", description = "Given a refresh token, returns an authorization token")
    @PostMapping(AuthenticationStatics.Endpoint.REFRESH)
    public ResponseEntity<RefreshResponseDTO> refresh(@RequestBody RefreshRequestDTO request)
            throws ForbiddenException {
        return ResponseEntity.ok(service.refresh(request));
    }

    @Operation(summary = "User logout", description = "User logout, making their tokens useless")
    @PostMapping(AuthenticationStatics.Endpoint.LOGOUT)
    public void logout(@RequestBody LogoutDTO request) throws BadRequestException {
        service.logout(request);
    }

    @PostMapping(AuthenticationStatics.Endpoint.RESET_PASSWORD_REQUEST)
    @Operation(summary = "Reset password request", description = "Send an email to allow a user to change their password")
    public ResponseEntity<ResetPasswordResponseDTO> resetPasswordRequest(@RequestBody ResetPasswordRequestDTO request)
            throws BadRequestException, NoContentException, IOException, MessagingException {
        return ResponseEntity.ok(service.resetPasswordRequest(request));
    }

    @PostMapping(AuthenticationStatics.Endpoint.RESET_PASSWORD)
    @Operation(summary = "Reset password using old password", description = "Change a user's password using their current password")
    public void resetPassword(@RequestBody ResetPasswordDTO request)
            throws ForbiddenException, BadRequestException, UnprocessableException, NoContentException {
        service.resetPassword(request, null);
    }

    @PostMapping(AuthenticationStatics.Endpoint.RESET_PASSWORD + "/{token}")
    @Operation(summary = "Reset password using auth token", description = "Change a user's password using an authorization token")
    public void resetPasswordWithToken(@RequestBody ResetPasswordDTO request, @PathVariable("token") String token)
            throws ForbiddenException, BadRequestException, UnprocessableException, NoContentException {
        service.resetPassword(request, token);
    }

    @PostMapping(AuthenticationStatics.Endpoint.VERIFY_EMAIL_REQUEST)
    @Operation(summary = "Send verify email request", description = "Send request to verify a user's email")
    public ResponseEntity<VerifyEmailResponseDTO> verifyEmailRequest(@RequestBody VerifyEmailRequestDTO request)
            throws BadRequestException, NoContentException, IOException, MessagingException {
        return ResponseEntity.ok(service.verifyEmailRequest(request));
    }

    @PostMapping(AuthenticationStatics.Endpoint.VERIFY_EMAIL + "/{token}")
    @Operation(summary = "Verify email", description = "Verify a user's mail using an authorization token")
    public void verifyEmail(@PathVariable("token") String token)
            throws ForbiddenException, BadRequestException, UnprocessableException, NoContentException {
        service.verifyEmail(token);
    }
}
