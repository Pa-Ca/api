package com.paca.paca.auth.utils;

import com.paca.paca.auth.statics.AuthenticationStatics;
import com.paca.paca.exception.exceptions.BadRequestException;
import com.paca.paca.exception.exceptions.UnprocessableException;
import org.passay.*;

import java.util.List;
import java.util.Map;
import java.util.Properties;

public class PassValidator {

    public static void validate(String password) {
        if (password == null) throw new BadRequestException("Password not found");

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
}
