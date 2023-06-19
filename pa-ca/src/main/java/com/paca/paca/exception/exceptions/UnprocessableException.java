package com.paca.paca.exception.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class UnprocessableException extends RuntimeException {

    private Integer code = 422;

    public UnprocessableException(String msg) {
        super(msg);
    }

    public UnprocessableException(String msg, Integer code) {
        super(msg);
        this.code = code;
    }

    public UnprocessableException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public Integer getCode() {
        return this.code;
    }
}
