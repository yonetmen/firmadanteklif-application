package com.firmadanteklif.application.exception;

public class InvalidUuidFormatException extends RuntimeException {

    private String message;
    private String redirectTarget;

    public InvalidUuidFormatException(String message, String redirectTarget) {
        this.message = message;
        this.redirectTarget = redirectTarget;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public String getRedirectTarget() {
        return redirectTarget;
    }
}