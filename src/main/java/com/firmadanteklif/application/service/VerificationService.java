package com.firmadanteklif.application.service;

import com.firmadanteklif.application.entity.SiteUser;
import com.firmadanteklif.application.entity.VerificationCode;
import com.firmadanteklif.application.entity.enums.VerificationEvent;
import com.firmadanteklif.application.entity.pojo.VerificationMessage;
import com.firmadanteklif.application.repository.UserRepository;
import com.firmadanteklif.application.repository.VerificationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

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

    public VerificationMessage findByIdAndVerificationType(String verificationId, VerificationEvent event, String email) {
        UUID uuid;
        try { // Check if incoming UUID has the right format.
            uuid = UUID.fromString(verificationId);
        } catch (Exception ex) {
            return new VerificationMessage(event, VerificationMessage.Type.danger, null,
                    messageSource.getMessage("user.activation.fail", null, Locale.getDefault()));
        }

        Optional<VerificationCode> codeOptional = verificationRepository.findByUuidAndVerificationEvent(uuid, event);
        if(codeOptional.isPresent()) { // Check if there is a valid pending activation code
            VerificationCode code = codeOptional.get();
            UUID userId = code.getOwnerId();
            boolean updated = updateUserStatus(userId);
            if(updated) {
                verificationRepository.delete(code);
                return new VerificationMessage(event, VerificationMessage.Type.success, email,
                        messageSource.getMessage("user.activation.success", null, Locale.getDefault()));
            }
        }
        return new VerificationMessage(event, VerificationMessage.Type.danger, email,
                messageSource.getMessage("user.activation.fail", null, Locale.getDefault()));
    }

    private boolean updateUserStatus(UUID userId) {
        Optional<SiteUser> byId = userRepository.findById(userId);
        if(byId.isPresent()) {
            SiteUser user = byId.get();
            user.setActive(true);
            userRepository.save(user);
            return true;
        }
        log.error("No user with given ID [" + userId + "] has found.");
        return false;
    }


    public UUID save(VerificationCode activation) {
        VerificationCode verificationCode = verificationRepository.save(activation);
        return verificationCode.getUuid();
    }
}
