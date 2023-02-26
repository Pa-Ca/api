package com.paca.paca.auth.utils;

import java.util.Map;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

import org.passay.LengthRule;
import org.passay.RuleResult;
import org.passay.PasswordData;
import org.passay.CharacterRule;
import org.passay.PasswordValidator;
import org.passay.EnglishCharacterData;
import org.passay.PropertiesMessageResolver;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.paca.paca.auth.statics.AuthenticationStatics;
import com.paca.paca.exception.exceptions.ConflictException;
import com.paca.paca.exception.exceptions.ForbiddenException;
import com.paca.paca.exception.exceptions.BadRequestException;
import com.paca.paca.exception.exceptions.UnprocessableException;

public class AuthUtils {
    public static void validateEmail(String email)
            throws BadRequestException, UnprocessableException, ConflictException {
        if (email == null)
            throw new BadRequestException("Email not found");

        if (!Pattern.compile(AuthenticationStatics.Auth.EMAIL_REGEX).matcher(email).matches()) {
            throw new UnprocessableException("Invalid email format", 0);
        }
    }
    
    public static void validatePassword(String password) {
        if (password == null)
            throw new BadRequestException("Password not found");

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
    }

    public static void validateRoles(List<String> roles) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!auth.getAuthorities().stream().anyMatch(a -> roles.contains(a.getAuthority()))) {
            throw new ForbiddenException("Unauthorized access for this operation");
        }
    }
}

