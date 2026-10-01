package com.eps.exception;

/**
 * Thrown when an authenticated user attempts to access resources outside their role permissions.
 */
public class UnauthorizedAccessException extends AppException {
    private static final long serialVersionUID = 1L;

    public UnauthorizedAccessException(String message) {
        super(message);
    }
}
