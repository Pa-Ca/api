package com.paca.paca.exception.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForbiddenException extends RuntimeException {

    private Integer code = 400;

    public ForbiddenException(String msg) {
        super(msg);
    }

    public ForbiddenException(String msg, Integer code) {
        super(msg);
        this.code = code;
    }

    public ForbiddenException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public Integer getCode() {
        return this.code;
    }
}
