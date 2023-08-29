package com.paca.paca.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

import com.paca.paca.exception.dto.ExceptionResponseDTO;
import com.paca.paca.exception.exceptions.BadRequestException;
import com.paca.paca.exception.exceptions.ConflictException;
import com.paca.paca.exception.exceptions.ForbiddenException;
import com.paca.paca.exception.exceptions.NotFoundException;
import com.paca.paca.exception.exceptions.UnprocessableException;

import java.time.ZonedDateTime;

@ControllerAdvice
public class ExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler(value = { BadRequestException.class })
    public ResponseEntity<Object> handleBadRequestException(BadRequestException e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ExceptionResponseDTO response = new ExceptionResponseDTO(
                e.getMessage(),
                status,
                ZonedDateTime.now(),
                e.getCode());
        return new ResponseEntity<>(response, status);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = { NotFoundException.class })
    public ResponseEntity<Object> handleNotFoundException(NotFoundException e) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ExceptionResponseDTO response = new ExceptionResponseDTO(
                e.getMessage(),
                status,
                ZonedDateTime.now(),
                e.getCode());
        return new ResponseEntity<>(response, status);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = { ConflictException.class })
    public ResponseEntity<Object> handleConflictException(ConflictException e) {
        HttpStatus status = HttpStatus.CONFLICT;
        ExceptionResponseDTO response = new ExceptionResponseDTO(
                e.getMessage(),
                status,
                ZonedDateTime.now(),
                e.getCode());
        return new ResponseEntity<>(response, status);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = { ForbiddenException.class })
    public ResponseEntity<Object> handleForbiddenException(ForbiddenException e) {
        HttpStatus status = HttpStatus.FORBIDDEN;
        ExceptionResponseDTO response = new ExceptionResponseDTO(
                e.getMessage(),
                status,
                ZonedDateTime.now(),
                e.getCode());
        return new ResponseEntity<>(response, status);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = { UnprocessableException.class })
    public ResponseEntity<Object> handleUnprocessableException(UnprocessableException e) {
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
        ExceptionResponseDTO response = new ExceptionResponseDTO(
                e.getMessage(),
                status,
                ZonedDateTime.now(),
                e.getCode());
        return new ResponseEntity<>(response, status);
    }
}
