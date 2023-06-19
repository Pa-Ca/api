package com.paca.paca.exception.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class MessagingException extends RuntimeException {

    private Integer code = 403;

    public MessagingException(String msg) {
        super(msg);
    }

    public MessagingException(String msg, Integer code) {
        super(msg);
        this.code = code;
    }

    public MessagingException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public Integer getCode() {
        return this.code;
    }
}
