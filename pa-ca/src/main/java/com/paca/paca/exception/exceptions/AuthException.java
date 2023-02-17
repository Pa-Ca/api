package com.paca.paca.exception.exceptions;

import org.springframework.security.core.AuthenticationException;

public class AuthException extends AuthenticationException {

    public AuthException(String msg) {
        super(msg);
    }

}