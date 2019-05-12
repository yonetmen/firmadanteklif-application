package com.firmadanteklif.application.service;

import com.firmadanteklif.application.domain.dto.FlashMessage;
import com.firmadanteklif.application.domain.entity.SiteUser;
import com.firmadanteklif.application.domain.entity.VerificationCode;
import com.firmadanteklif.application.domain.enums.VerificationEvent;
import com.firmadanteklif.application.exception.UserNotFoundException;
import com.firmadanteklif.application.repository.UserRepository;
import com.firmadanteklif.application.repository.VerificationRepository;
import com.firmadanteklif.application.utility.FlashUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Map;
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

    public UUID save(VerificationCode activation) {
        VerificationCode verificationCode = verificationRepository.save(activation);
        return verificationCode.getUuid();
    }

    public FlashMessage getVerificationResultForUserRegister(UUID verificationId, VerificationEvent event, String email) {

        FlashMessage flashMessage = new FlashMessage();
        Optional<VerificationCode> codeOptional =
                verificationRepository.findByUuidAndVerificationEvent(verificationId, event);

        if (codeOptional.isPresent()) { // Check if there is a valid pending activation code
            VerificationCode code = codeOptional.get();
            UUID userId = code.getOwnerId();
            boolean updated = updateUserStatus(userId, email);

            if (updated) {
                verificationRepository.delete(code);
                flashMessage.setKind(FlashUtility.FLASH_SUCCESS);
                flashMessage.setMessage(messageSource
                        .getMessage("user.activation.success", null, Locale.getDefault()));
                return flashMessage;
            }
        }

        flashMessage.setKind(FlashUtility.FLASH_DANGER);
        flashMessage.setMessage(messageSource
                .getMessage("user.activation.fail", null, Locale.getDefault()));
        return flashMessage;
    }

    private boolean updateUserStatus(UUID userId, String email) {
        Optional<SiteUser> byId = userRepository.findById(userId);
        if (byId.isPresent() && byId.get().getEmail().equalsIgnoreCase(email)) {
            SiteUser user = byId.get();
            user.setActive(true);
            userRepository.save(user);
            return true;
        }
        log.error("No User with given ID [" + userId + "] or given E-mail [" + email + "] has found.");
        return false;
    }

    public FlashMessage getVerificationResultForPasswordReset(UUID verificationId, VerificationEvent event, String email) {

        FlashMessage flashMessage = new FlashMessage();

        Optional<VerificationCode> codeOptional =
                verificationRepository.findByUuidAndVerificationEvent(verificationId, event);

        if (codeOptional.isPresent()) { // Check if there is a valid pending password-reset code
            VerificationCode code = codeOptional.get();

            Optional<SiteUser> userOptional = userRepository.findByEmail(email);
            // Todo: Exception message is not generic. Refactor it.
            SiteUser siteUser = userOptional.orElseThrow(() -> new UserNotFoundException(email));

            if (siteUser.getUuid().equals(code.getOwnerId())) {
                flashMessage.setKind(FlashUtility.FLASH_SUCCESS);
                flashMessage.setMessage(messageSource
                        .getMessage("user.password.reset.link.valid", null, Locale.getDefault()));
                return flashMessage;
            }
        }

        flashMessage.setKind(FlashUtility.FLASH_SUCCESS);
        flashMessage.setMessage(messageSource
                .getMessage("user.password.reset.fail", null, Locale.getDefault()));
        return flashMessage;
    }
}
