package com.eps.exception;

import java.util.Collections;
import java.util.List;

/**
 * Thrown when domain validation rules are violated.
 */
public class ValidationException extends AppException {
    private static final long serialVersionUID = 1L;

    private final List<String> errorMessages;

    public ValidationException(String message) {
        super(message);
        this.errorMessages = Collections.singletonList(message);
    }

    public ValidationException(List<String> errorMessages) {
        super(errorMessages != null && !errorMessages.isEmpty() ? errorMessages.get(0) : "Validation failed");
        this.errorMessages = errorMessages != null ? errorMessages : Collections.emptyList();
    }

    public List<String> getErrorMessages() {
        return errorMessages;
    }
}
