package com.paca.paca.exception.dto;

import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

public class ExceptionResponseDTO {
    private final String msg;
    private final HttpStatus status;
    private final ZonedDateTime timestamp;
    private final Integer code;

    public ExceptionResponseDTO(
            String msg,
            HttpStatus status,
            ZonedDateTime timestamp,
            Integer code) {
        this.msg = msg;
        this.status = status;
        this.timestamp = timestamp;
        this.code = code;
    }

    public Integer getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.msg;
    }

    public HttpStatus getStatus() {
        return this.status;
    }

    public ZonedDateTime getTimestamp() {
        return this.timestamp;
    }

}
