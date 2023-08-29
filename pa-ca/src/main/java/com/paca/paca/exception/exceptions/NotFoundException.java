package com.paca.paca.exception.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {

    private Integer code = 404;

    public NotFoundException(String msg) {
        super(msg);
    }

    public NotFoundException(String msg, Integer code) {
        super(msg);
        this.code = code;
    }

    public NotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public Integer getCode() {
        return this.code;
    }
}
