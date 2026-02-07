package com.jobs.jobtracker.exceptions;

public class AccountInactiveException extends RuntimeException{
    public AccountInactiveException(String message) {
        super(message);
    }
}
