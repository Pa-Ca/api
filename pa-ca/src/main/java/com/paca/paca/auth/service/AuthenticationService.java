package com.paca.paca.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

import org.passay.RuleResult;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.CharacterRule;
import org.passay.PasswordValidator;
import org.passay.EnglishCharacterData;
import org.passay.PropertiesMessageResolver;

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
import com.paca.paca.auth.statics.AuthenticationStatics;

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
        if (email == null) {
            throw new BadRequestException("Email not found");
        }
        if (!Pattern.compile(AuthenticationStatics.Auth.EMAIL_REGEX).matcher(email).matches()) {
            throw new UnprocessableException("Invalid email format", 0);
        }
        if (repository.existsByEmail(email)) {
            throw new ConflictException("User already exists", 1);
        }

        // Password Validation
        String password = request.getPassword();
        if (password == null) {
            throw new BadRequestException("Password not found");
        }
        Properties props = new Properties();
        for (Map.Entry<String, String> entry : AuthenticationStatics.Auth.PASSWORD_ERRORS_PROPS
                .entrySet()) {
            props.setProperty(entry.getKey(), entry.getValue());
        }
        PasswordValidator validator = new PasswordValidator(
                new PropertiesMessageResolver(props),
                // length
                new LengthRule(
                        AuthenticationStatics.Auth.PASS_MIN_LENGTH,
                        AuthenticationStatics.Auth.PASS_MAX_LENGTH),
                // at least one upper-case character
                new CharacterRule(EnglishCharacterData.UpperCase, 1),
                // at least one lower-case character
                new CharacterRule(EnglishCharacterData.LowerCase, 1),
                // at least one digit character
                new CharacterRule(EnglishCharacterData.Digit, 1),
                // at least one symbol (special character)
                new CharacterRule(EnglishCharacterData.Special, 1));
        RuleResult result = validator.validate(new PasswordData(password));
        if (!result.isValid()) {
            List<String> codes = validator.getMessages(result);
            throw new UnprocessableException("Invalid password", Integer.parseInt(codes.get(0)));
        }

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
            throw new NoContentException("User don't exists", 8);
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
