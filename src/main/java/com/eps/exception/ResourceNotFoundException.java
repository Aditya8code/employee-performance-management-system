package com.eps.exception;

/**
 * Thrown when an entity (Employee, Cycle, Evaluation, Department, Criterion) cannot be found by ID.
 */
public class ResourceNotFoundException extends AppException {
    private static final long serialVersionUID = 1L;

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
