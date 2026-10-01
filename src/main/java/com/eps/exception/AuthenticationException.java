package com.eps.exception;

/**
 * Thrown when credentials fail or user account is disabled.
 */
public class AuthenticationException extends AppException {
    private static final long serialVersionUID = 1L;

    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
