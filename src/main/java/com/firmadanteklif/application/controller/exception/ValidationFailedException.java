package com.firmadanteklif.application.controller.exception;

public class ValidationFailedException extends RuntimeException {

    private String message;

    public ValidationFailedException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}