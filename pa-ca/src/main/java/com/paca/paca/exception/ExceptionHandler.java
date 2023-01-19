package com.paca.paca.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.time.ZonedDateTime;

@ControllerAdvice
public class ExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler(value = { BadRequestException.class })
    public ResponseEntity<Object> handleBadRequestException(BadRequestException e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ExceptionResponse response = new ExceptionResponse(e.getMessage(), status, ZonedDateTime.now());
        return new ResponseEntity<>(response, status);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = { NoContentException.class })
    public ResponseEntity<Object> handleBadNoContentException(NoContentException e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ExceptionResponse response = new ExceptionResponse(e.getMessage(), status, ZonedDateTime.now());
        return new ResponseEntity<>(response, status);
    }
}
