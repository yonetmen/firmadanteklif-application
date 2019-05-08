package com.firmadanteklif.application.exception;

public class InvalidUuidFormatException extends RuntimeException {

    private String message;

    public InvalidUuidFormatException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}