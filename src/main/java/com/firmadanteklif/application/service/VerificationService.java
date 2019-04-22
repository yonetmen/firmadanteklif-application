package com.firmadanteklif.application.service;

import com.firmadanteklif.application.entity.SiteUser;
import com.firmadanteklif.application.entity.VerificationCode;
import com.firmadanteklif.application.entity.enums.VerificationType;
import com.firmadanteklif.application.repository.UserRepository;
import com.firmadanteklif.application.repository.VerificationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class VerificationService {

    private VerificationRepository verificationRepository;
    private UserRepository userRepository;

    @Autowired
    public VerificationService(VerificationRepository verificationRepository, UserRepository userRepository) {
        this.verificationRepository = verificationRepository;
        this.userRepository = userRepository;
    }

    public String findByIdAndVerificationType(UUID verificationId, VerificationType type) {
        Optional<VerificationCode> codeOptional = verificationRepository.findByUuidAndVerificationType(verificationId, type);
        if(codeOptional.isPresent()) {
            VerificationCode code = codeOptional.get();
            if(code.isConsumed()) {
                throw new RuntimeException("This Activation code is already used.");
            }
            UUID userId = code.getOwnerId();
            String userEmail = updateUserStatus(userId);
            if(userEmail != null) {
                code.setConsumed(true);
                verificationRepository.save(code);
                return userEmail;
            }
        }
        throw new RuntimeException("No user with given ID has found.");
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
