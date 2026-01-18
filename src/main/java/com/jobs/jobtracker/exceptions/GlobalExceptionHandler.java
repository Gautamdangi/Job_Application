package com.jobs.jobtracker.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailException.class)
    public ResponseEntity<Map<String, String>> handleEmailExists(
            EmailException ex) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)   // 409 (BEST for duplicates)
                .body(Map.of("message", ex.getMessage()));
    }
}

