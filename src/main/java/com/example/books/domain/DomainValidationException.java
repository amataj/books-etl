package com.example.books.domain;

/**
 * Exception thrown when domain invariants are violated.
 */
public class DomainValidationException extends RuntimeException {

    public DomainValidationException(String message) {
        super(message);
    }
}
