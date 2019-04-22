package com.firmadanteklif.application.entity.pojo;

import lombok.Data;

@Data
public class VerificationMessage {

    private String key;
    private String value;
    private String email;

    enum Key {
        REGISTER, FORGOT_PASS, CHANGE_EMAIL
    }

    enum Value {
        SUCCESS, FAIL
    }

    public VerificationMessage(Key key, Value value, String email) {
        this.key = key.name();
        this.value = value.name();
        this.email = email;
    }
}
