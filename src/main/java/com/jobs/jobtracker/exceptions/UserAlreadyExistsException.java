package com.jobs.jobtracker.exceptions;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException (String message){
        super(message);
    }
}
