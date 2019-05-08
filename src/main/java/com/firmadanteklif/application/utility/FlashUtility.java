package com.firmadanteklif.application.utility;

import com.firmadanteklif.application.domain.dto.FlashMessage;

public class FlashUtility {

    private FlashUtility() { /* No Instance allowed */ }

    public static final String FLASH_SUCCESS = "success";
    public static final String FLASH_DANGER = "danger";

    public static FlashMessage getFlashMessage(String kind, String message) {
        FlashMessage flashMessage = new FlashMessage();
        flashMessage.setKind(kind);
        flashMessage.setMessage(message);
        return flashMessage;
    }
}
