package com.firmadanteklif.application.entity.pojo;

import com.firmadanteklif.application.entity.enums.VerificationEvent;
import lombok.Data;

@Data
public class VerificationMessage {

    private String event;
    private String type;
    private String email;
    private String message;


    public enum Type {
        success, danger, warning, info
    }

    public VerificationMessage(VerificationEvent event, Type type, String email, String message) {
        this.event = event.name();
        this.type = type.name();
        this.email = email;
        this.message = message;
    }
}
