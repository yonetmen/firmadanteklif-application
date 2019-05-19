package com.firmadanteklif.application.exception;

public class UserNotFoundException extends RuntimeException {

    private String email;

    public UserNotFoundException(String email) {
        this.email = email;
    }

    @Override
    public String getMessage() {
        return "'" + email + "' adresi bulunamadı.";
    }
}