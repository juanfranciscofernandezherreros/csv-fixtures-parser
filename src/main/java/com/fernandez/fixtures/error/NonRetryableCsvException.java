package com.fernandez.fixtures.error;

public class NonRetryableCsvException extends RuntimeException {

    public NonRetryableCsvException(String message, Throwable cause) {
        super(message, cause);
    }
}
