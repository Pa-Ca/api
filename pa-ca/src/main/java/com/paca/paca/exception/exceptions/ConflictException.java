package com.paca.paca.exception.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ConflictException extends RuntimeException {

    private Integer code = 409;

    public ConflictException(String msg) {
        super(msg);
    }

    public ConflictException(String msg, Integer code) {
        super(msg);
        this.code = code;
    }

    public ConflictException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public Integer getcode() {
        return this.code;
    }
}