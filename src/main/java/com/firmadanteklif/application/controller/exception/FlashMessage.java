package com.firmadanteklif.application.controller.exception;

import lombok.Data;

@Data
public class FlashMessage {

    public FlashMessage(String flashKind, String flashMessage) {
        this.flashKind = flashKind;
        this.flashMessage = flashMessage;
    }

    private String flashKind;
    private String flashMessage;
}
