package com.example.repository;

/**
 * Exception class for repository errors.
 */

public class RepositoryException extends RuntimeException {
    public RepositoryException(String message, Throwable cause) {
        super(message, cause);
    }

    public RepositoryException() {
    }
}
