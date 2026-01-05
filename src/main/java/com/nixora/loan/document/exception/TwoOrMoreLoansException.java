package com.nixora.loan.document.exception;

public class TwoOrMoreLoansException extends RuntimeException {
    public TwoOrMoreLoansException(String message) {
        super(message);
    }
}
