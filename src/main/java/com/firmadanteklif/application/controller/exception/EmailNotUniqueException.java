package com.firmadanteklif.application.controller.exception;

public class EmailNotUniqueException extends RuntimeException {

    private String message;

    public EmailNotUniqueException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}