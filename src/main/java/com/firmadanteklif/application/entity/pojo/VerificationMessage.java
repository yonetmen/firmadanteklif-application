package com.firmadanteklif.application.entity.pojo;

import com.firmadanteklif.application.entity.enums.VerificationType;
import lombok.Data;

@Data
public class VerificationMessage {

    private String type;
    private String value;
    private String email;
    private String message;


    public enum Value {
        success, danger, warning, info
    }

    public VerificationMessage(VerificationType type, Value value, String email, String message) {
        this.type = type.name();
        this.value = value.name();
        this.email = email;
        this.message = message;
    }
}
