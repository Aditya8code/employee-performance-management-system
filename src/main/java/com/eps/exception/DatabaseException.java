package com.eps.exception;

/**
 * Thrown when low-level JDBC SQL or connection pool errors occur.
 */
public class DatabaseException extends AppException {
    private static final long serialVersionUID = 1L;

    public DatabaseException(String message) {
        super(message);
    }

    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
