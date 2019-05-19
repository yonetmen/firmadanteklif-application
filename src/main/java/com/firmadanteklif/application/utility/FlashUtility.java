package com.firmadanteklif.application.utility;

import com.firmadanteklif.application.domain.dto.FlashMessage;
import com.firmadanteklif.application.domain.enums.VerificationEvent;

import java.util.HashMap;
import java.util.Map;

public class FlashUtility {

    private FlashUtility() { /* No Instance allowed */ }

    public static final String FLASH_SUCCESS = "success";
    public static final String FLASH_DANGER = "danger";
    public static final String FLASH_INFO = "info";
    public static final String FLASH_WARNING = "warning";

    public static FlashMessage getFlashMessage(String kind, String message) {
        FlashMessage flashMessage = new FlashMessage();
        flashMessage.setKind(kind);
        flashMessage.setMessage(message);
        return flashMessage;
    }

//    public static Map<String, String> getMessageCodeByEvent(VerificationEvent event) {
//        Map<String, String> returnMap = new HashMap<>();
//        switch (event) {
//            case REGISTER:
//                returnMap.put(FLASH_SUCCESS, "user.activation.success");
//                returnMap.put(FLASH_DANGER, "user.activation.fail");
//                break;
//            case FORGOT_PASSWORD:
//                returnMap.put(FLASH_DANGER, "user.password.reset.fail");
//                returnMap.put(FLASH_SUCCESS, "user.password.reset.link.valid");
//                break;
//            case CHANGE_EMAIL:
//                break;
//        }
//
//        return returnMap;
//    }
}
