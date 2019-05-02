package com.firmadanteklif.application.service;

import com.firmadanteklif.application.controller.exception.ValidationFailedException;
import com.firmadanteklif.application.domain.dto.FlashMessage;
import com.firmadanteklif.application.domain.entity.SiteUser;
import com.firmadanteklif.application.domain.entity.VerificationCode;
import com.firmadanteklif.application.repository.UserRepository;
import com.firmadanteklif.application.repository.VerificationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class VerificationService {

    private VerificationRepository verificationRepository;
    private UserRepository userRepository;
    private MessageSource messageSource;

    @Autowired
    public VerificationService(VerificationRepository verificationRepository, UserRepository userRepository,
                               @Qualifier("messageSource") MessageSource messageSource) {
        this.verificationRepository = verificationRepository;
        this.userRepository = userRepository;
        this.messageSource = messageSource;
    }

    public void processPendingActivationByVerificationId(String verificationId, Model model, String emailFromActivationLink) {
        UUID uuid;
        try { // Check if incoming UUID is valid.
            uuid = UUID.fromString(verificationId);
        } catch (Exception ex) {
            throw new ValidationFailedException(messageSource
                    .getMessage("user.activation.fail", null, Locale.getDefault()));
        }

        Optional<VerificationCode> codeOptional = verificationRepository.findByUuid(uuid);
        if(codeOptional.isPresent()) { // Check if there is a pending activation code
            VerificationCode code = codeOptional.get();
            UUID userId = code.getOwnerId();
            String userEmail = updateUserStatus(userId);
            if(userEmail != null && userEmail.equalsIgnoreCase(emailFromActivationLink)) {
                verificationRepository.delete(code);
                // Save FlashMessage in Model
                FlashMessage flashMessage = new FlashMessage();
                flashMessage.setKind("success");
                flashMessage.setMessage(messageSource
                        .getMessage("user.activation.success", null, Locale.getDefault()));
                model.addAttribute("flashMessage", flashMessage);
                // Save SiteUser in Model
                SiteUser user = new SiteUser();
                user.setEmail(userEmail);
                model.addAttribute("user", user);
            }
        }
        throw new ValidationFailedException(messageSource
                .getMessage("user.activation.fail", null, Locale.getDefault()));
    }

    private String updateUserStatus(UUID userId) {
        Optional<SiteUser> byId = userRepository.findById(userId);
        if(byId.isPresent()) {
            SiteUser user = byId.get();
            user.setActive(true);
            userRepository.save(user);
            return user.getEmail();
        }
        log.error("No user with given ID [" + userId + "] has found.");
        return null;
    }


    public UUID save(VerificationCode activation) {
        VerificationCode verificationCode = verificationRepository.save(activation);
        return verificationCode.getUuid();
    }
}
