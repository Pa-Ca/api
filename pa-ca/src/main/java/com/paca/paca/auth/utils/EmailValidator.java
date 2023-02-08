package com.paca.paca.auth.utils;

import com.paca.paca.auth.statics.AuthenticationStatics;
import com.paca.paca.exception.exceptions.BadRequestException;
import com.paca.paca.exception.exceptions.ConflictException;
import com.paca.paca.exception.exceptions.UnprocessableException;

import java.util.regex.Pattern;

public class EmailValidator {

    public static void validate(String email) throws BadRequestException, UnprocessableException, ConflictException {
        if (email == null) throw new BadRequestException("Email not found");

        if (!Pattern.compile(AuthenticationStatics.Auth.EMAIL_REGEX).matcher(email).matches()) {
            throw new UnprocessableException("Invalid email format", 0);
        }
    }

}
