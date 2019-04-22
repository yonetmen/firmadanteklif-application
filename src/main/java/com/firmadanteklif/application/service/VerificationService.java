package com.firmadanteklif.application.service;

import com.firmadanteklif.application.entity.SiteUser;
import com.firmadanteklif.application.entity.VerificationCode;
import com.firmadanteklif.application.entity.enums.VerificationType;
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

    public VerificationMessage findByIdAndVerificationType(String verificationId, VerificationType type) {
        UUID uuid;
        try {
            uuid = UUID.fromString(verificationId);
        } catch (Exception ex) {
            return new VerificationMessage(type, VerificationMessage.Value.danger, null,
                    messageSource.getMessage("user.register.fail", null, Locale.getDefault()));
        }

        Optional<VerificationCode> codeOptional = verificationRepository.findByUuidAndVerificationType(uuid, type);
        if(codeOptional.isPresent()) {
            VerificationCode code = codeOptional.get();
            UUID userId = code.getOwnerId();
            String userEmail = updateUserStatus(userId);
            if(userEmail != null) {
                verificationRepository.delete(code);
                return new VerificationMessage(type, VerificationMessage.Value.success, userEmail,
                        messageSource.getMessage("user.register.success", null, Locale.getDefault()));
            }
        }
        return new VerificationMessage(type, VerificationMessage.Value.danger, null,
                messageSource.getMessage("user.register.fail", null, Locale.getDefault()));
    }

    private String updateUserStatus(UUID userId) {
        Optional<SiteUser> byId = userRepository.findById(userId);
        if(byId.isPresent()) {
            SiteUser user = byId.get();
            user.setActive(true);
            SiteUser saved = userRepository.save(user);
            return saved.getEmail();
        }
        log.error("No user with given ID [" + userId + "] has found.");
        return null;
    }


    public UUID save(VerificationCode activation) {
        VerificationCode verificationCode = verificationRepository.save(activation);
        return verificationCode.getUuid();
    }
}
