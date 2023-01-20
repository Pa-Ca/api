package com.paca.paca.exception.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {

    private Integer code = 400;

    public BadRequestException(String msg) {
        super(msg);
    }

    public BadRequestException(String msg, Integer code) {
        super(msg);
        this.code = code;
    }

    public BadRequestException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public Integer getCode() {
        return this.code;
    }
}
