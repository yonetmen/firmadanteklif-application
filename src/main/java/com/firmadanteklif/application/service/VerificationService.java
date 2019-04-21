package com.firmadanteklif.application.service;

import com.firmadanteklif.application.entity.VerificationCode;
import com.firmadanteklif.application.repository.VerificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.UUID;

@Service
public class VerificationService {

    private VerificationRepository verificationRepository;

    @Autowired
    public VerificationService(VerificationRepository verificationRepository) {
        this.verificationRepository = verificationRepository;
    }


    public UUID save(VerificationCode activation) {
        VerificationCode verificationCode = verificationRepository.save(activation);
        return verificationCode.getUuid();
    }
}
