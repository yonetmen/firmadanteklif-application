package com.firmadanteklif.application.controller;

import com.firmadanteklif.application.entity.SiteUser;
import com.firmadanteklif.application.entity.enums.VerificationEvent;
import com.firmadanteklif.application.entity.pojo.VerificationMessage;
import com.firmadanteklif.application.service.VerificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Slf4j
@Controller
public class VerificationController {

    private VerificationService verificationService;

    @Autowired
    public VerificationController(VerificationService verificationService) {
        this.verificationService = verificationService;
    }

    @GetMapping("/activation/{email}/{verificationId}")
    public String activateAccount(@PathVariable String verificationId, @PathVariable String email, Model model) {
        log.info("VERIFICATION CONTROLLER: V.ID: " + verificationId);
        VerificationMessage verificationMessage = verificationService.findByIdAndVerificationType(verificationId, VerificationEvent.REGISTER, email);
        SiteUser user = new SiteUser();
        if(verificationMessage.getEmail() != null)
            user.setEmail(verificationMessage.getEmail());
        model.addAttribute("user", user);
        model.addAttribute("verificationMessage", verificationMessage);
        return "/user/login";
    }

}
