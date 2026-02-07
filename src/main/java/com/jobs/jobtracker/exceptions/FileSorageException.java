package com.jobs.jobtracker.exceptions;

import ch.qos.logback.core.joran.conditional.ThenAction;

public class FileSorageException extends RuntimeException {
    public FileSorageException(String message) {
        super(message);
    }
    public FileSorageException(String message, Throwable cause){
        super(message, cause);
    }
}
