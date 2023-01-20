package com.paca.paca.exception.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NO_CONTENT)
public class NoContentException extends RuntimeException {

    private Integer code = 204;

    public NoContentException(String msg) {
        super(msg);
    }

    public NoContentException(String msg, Integer code) {
        super(msg);
        this.code = code;
    }

    public NoContentException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public Integer getCode() {
        return this.code;
    }
}
