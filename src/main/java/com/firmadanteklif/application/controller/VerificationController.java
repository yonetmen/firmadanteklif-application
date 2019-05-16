package com.firmadanteklif.application.controller;

import com.firmadanteklif.application.domain.dto.FlashMessage;
import com.firmadanteklif.application.domain.entity.SiteUser;
import com.firmadanteklif.application.domain.enums.VerificationEvent;
import com.firmadanteklif.application.exception.InvalidUuidFormatException;
import com.firmadanteklif.application.exception.UserNotFoundException;
import com.firmadanteklif.application.service.UserService;
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
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Controller
public class VerificationController {

    private VerificationService verificationService;
    private MessageSource messageSource;
    private UserService userService;

    @Autowired
    public VerificationController(VerificationService verificationService,
                                  MessageSource messageSource,
                                  UserService userService) {
        this.verificationService = verificationService;
        this.messageSource = messageSource;
        this.userService = userService;
    }

    @GetMapping("/activation/{email}/{verificationId}")
    public String activateAccount(@PathVariable String verificationId, @PathVariable String email, Model model) {

        UUID uuid = validateVerificationUUID(verificationId);
        String returnPage = "home";

        FlashMessage flashMessage = verificationService.getVerificationResultForUserRegister(uuid, VerificationEvent.REGISTER, email);
        SiteUser user = new SiteUser();
        if(flashMessage.getKind().equalsIgnoreCase(FlashUtility.FLASH_SUCCESS)) {
            user.setEmail(email);
            returnPage = "/user/login";
        }
        model.addAttribute("user", user);
        model.addAttribute("flashMessage", flashMessage);
        return returnPage;
    }

    @GetMapping("/reset-password/{email}/{verificationId}")
    public String resetPassword(@PathVariable String verificationId, @PathVariable String email, Model model) {

        UUID uuid = validateVerificationUUID(verificationId);
        SiteUser user = validateSiteUser(email);

        FlashMessage flashMessage = verificationService.getVerificationResultForPasswordReset(uuid, VerificationEvent.FORGOT_PASSWORD, email);

        if(flashMessage.getKind().equalsIgnoreCase(FlashUtility.FLASH_SUCCESS)) {
            user.setEmail(email);
        }
        model.addAttribute("user", user);
        model.addAttribute("flashMessage", flashMessage);
        return "/user/password-new";
    }

    private UUID validateVerificationUUID(String verificationId) {
        try { // Check if incoming UUID has the right format.
            return UUID.fromString(verificationId);
        } catch (Exception ex) {
            String errorMessage = messageSource.getMessage("user.activation.fail", null, Locale.getDefault());
            throw new InvalidUuidFormatException(errorMessage, "home");
        }
    }

    private SiteUser validateSiteUser(String email) {
        Optional<SiteUser> userOptional = userService.findUserByEmail(email);
        return userOptional.orElseThrow(()-> new UserNotFoundException(email));
    }
}
