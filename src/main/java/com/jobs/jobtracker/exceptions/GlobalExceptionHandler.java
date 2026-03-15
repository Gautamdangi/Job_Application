package com.jobs.jobtracker.exceptions;

import com.jobs.jobtracker.dto.ErrorResponse;
import com.jobs.jobtracker.exceptions.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExists(
           UserAlreadyExistsException existsException,
            HttpServletRequest request
    ) {
        log.error("User already exists {}: ", existsException.getMessage());
        ErrorResponse error = new ErrorResponse(
                    HttpStatus.CONFLICT.value(),
                existsException.getMessage(),
                        request.getRequestURI()
                        );


                return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
            }

            @ExceptionHandler(InvalidCredentialsException.class)
            public ResponseEntity<ErrorResponse> handleInvalidCredentials(
                    InvalidCredentialsException invalidCr,
                HttpServletRequest request1
    ){
                log.error("Invalid Credentials :{} ", invalidCr.getMessage());
                ErrorResponse errorResponse = new ErrorResponse(
                        HttpStatus.UNAUTHORIZED.value(),
                        invalidCr.getMessage(),
                        request1.getRequestURI()
                );
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);

        }

    @ExceptionHandler(JobNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleJobNotFound(
            JobNotFoundException jobNotFoundException,
            HttpServletRequest request
    ) {
        log.error("Job Not Found", jobNotFoundException.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                jobNotFoundException.getMessage(),
                request.getRequestURI()
        );
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);

    }

    @ExceptionHandler(value = ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(
            ResourceNotFoundException resourceNotFoundException,
            HttpServletRequest request
    ){
        log.error("Resource not found :{} ", resourceNotFoundException.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                resourceNotFoundException.getMessage(),
                request.getRequestURI()
        );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorized(
            UnauthorizedException exception,
            HttpServletRequest request
    ) {
        log.error("User Not authorized to access this {}: " , exception.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.UNAUTHORIZED.value(),
                exception.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);

    }
    // fileStorageException
    @ExceptionHandler(FileSorageException.class)
    public ResponseEntity<ErrorResponse> handleStorage(
            FileSorageException fileException,
            HttpServletRequest request
    ){
        log.error("Your file Size is above the specified size {}: ",fileException.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE.value(),
                fileException.getMessage(),
                request.getRequestURI()
                );
        return ResponseEntity.status(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE).body(errorResponse);

    }
    //AccountInactive
    @ExceptionHandler(AccountInactiveException.class)
    public  ResponseEntity<ErrorResponse> handleAccountInactive(
            AccountInactiveException inactiveException,
            HttpServletRequest request
    ){
        log.error("Wrong Credentials {}:", inactiveException.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                inactiveException.getMessage(),
                request.getRequestURI()
        );
        return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
    //AlreadyJobApplied
    @ExceptionHandler(AlreadyJobAppliedException.class)
    public  ResponseEntity<ErrorResponse> handleAlreadyApplied(
          AlreadyJobAppliedException alreadyAppliedException,
            HttpServletRequest request
    ){
        log.error("Already applied for thi job{} :", alreadyAppliedException.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                alreadyAppliedException.getMessage(),
                request.getRequestURI()
        );
        return  ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }


            @ExceptionHandler(EmailException.class)
            public ResponseEntity<Map<String, String>> handleEmailExists(
                    EmailException ex) {

                return ResponseEntity
                        .status(HttpStatus.CONFLICT)   // 409 (BEST for duplicates)
                        .body(Map.of("message", ex.getMessage()));
            }
        }


