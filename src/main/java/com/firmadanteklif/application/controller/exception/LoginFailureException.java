package com.firmadanteklif.application.controller.exception;

public class LoginFailureException extends RuntimeException {

    private String message;

    public LoginFailureException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}