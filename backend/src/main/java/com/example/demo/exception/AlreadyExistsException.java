package com.example.demo.exception;

/**
 * Exception thrown when trying to create a resource that already exists.
 */
public class AlreadyExistsException extends RuntimeException {

    public AlreadyExistsException(String message) {
        super(message);
    }

    public AlreadyExistsException(String resourceName, String fieldName, String fieldValue) {
        super(String.format("%s with %s '%s' already exists", resourceName, fieldName, fieldValue));
    }
}
