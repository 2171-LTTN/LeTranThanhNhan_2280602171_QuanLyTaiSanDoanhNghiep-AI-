package com.lttn.quanlytaisan.exception;

/**
 * Custom business exception for application-specific errors.
 * Used when business rules are violated.
 */
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}
