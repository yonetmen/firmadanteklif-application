package com.firmadanteklif.application.controller.exception;

public class EmailNotUniqueException extends Exception {
    private String email;

    public static EmailNotUniqueException createWith(String email) {
        return new EmailNotUniqueException(email);
    }

    private EmailNotUniqueException(String username) {
        this.email = username;
    }

    @Override
    public String getMessage() {
        return "Email adresi: (" + email + ") sitemize kayitli.";
    }
}
