package com.backend.orderservice.exception;

/**
 * Exception thrown when payment processing fails
 * Used in the Saga pattern to trigger compensation/rollback actions
 */
public class PaymentException extends RuntimeException {

    public PaymentException(String message) {
        super(message);
    }

    public PaymentException(String message, Throwable cause) {
        super(message, cause);
    }
}