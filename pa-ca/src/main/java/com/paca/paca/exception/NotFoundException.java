package com.paca.paca.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus
public class NotFoundException extends RuntimeException {
    public NotFoundException(String msg) { super(msg); }
}
