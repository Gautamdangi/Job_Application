package com.jobs.jobtracker.exceptions;

public class AlreadyJobAppliedException extends RuntimeException {
    public AlreadyJobAppliedException(String message) {
        super(message);
    }
}
