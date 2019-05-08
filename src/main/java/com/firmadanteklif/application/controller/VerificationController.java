package com.firmadanteklif.application.controller;

import com.firmadanteklif.application.domain.dto.FlashMessage;
import com.firmadanteklif.application.domain.entity.SiteUser;
import com.firmadanteklif.application.domain.enums.VerificationEvent;
import com.firmadanteklif.application.exception.InvalidUuidFormatException;
import com.firmadanteklif.application.service.VerificationService;
import com.firmadanteklif.application.utility.FlashUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Locale;
import java.util.UUID;

@Slf4j
@Controller
public class VerificationController {

    private VerificationService verificationService;
    private MessageSource messageSource;

    @Autowired
    public VerificationController(VerificationService verificationService,
                                  MessageSource messageSource) {
        this.verificationService = verificationService;
        this.messageSource = messageSource;
    }

    @GetMapping("/activation/{email}/{verificationId}")
    public String activateAccount(@PathVariable String verificationId, @PathVariable String email, Model model) {
        log.info("VerificationCode: " + verificationId);

        UUID uuid = validateVerificationUUID(verificationId);

        FlashMessage flashMessage = verificationService.getVerificationOpResult(uuid, VerificationEvent.REGISTER, email);
        SiteUser user = new SiteUser();
        if(flashMessage.getKind().equalsIgnoreCase(FlashUtility.FLASH_SUCCESS))
            user.setEmail(email);
        model.addAttribute("user", user);
        model.addAttribute("flashMessage", flashMessage);
        return "/user/login";
    }

    private UUID validateVerificationUUID(String verificationId) {
        try { // Check if incoming UUID has the right format.
            return UUID.fromString(verificationId);
        } catch (Exception ex) {
            throw new InvalidUuidFormatException(messageSource
                    .getMessage("user.activation.fail", null, Locale.getDefault()));
        }
    }

}
