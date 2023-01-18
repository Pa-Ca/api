package com.paca.paca.exception;

import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

public class ExceptionResponse {
    private final String msg;
    private final HttpStatus status;
    private final ZonedDateTime timestamp;

    ExceptionResponse(String msg, HttpStatus status, ZonedDateTime timestamp) {
        this.msg = msg;
        this.status = status;
        this.timestamp = timestamp;
    }

    public String getMessage() { return this.msg; }
    public HttpStatus getStatus() { return this.status; }
    public ZonedDateTime getTimestamp() { return this.timestamp; }

}
