package com.firmadanteklif.application.controller.exception;

public class NotValidatedException extends RuntimeException {

    private String message;

    public NotValidatedException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}