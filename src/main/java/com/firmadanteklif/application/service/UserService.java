package com.firmadanteklif.application.service;

import com.firmadanteklif.application.domain.entity.SiteUser;
import com.firmadanteklif.application.domain.enums.VerificationEvent;
import com.firmadanteklif.application.domain.dto.VerificationMessage;
import com.firmadanteklif.application.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.Locale;
import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;
    private MailService mailService;
    private MessageSource messageSource;

    @Autowired
    public UserService(UserRepository userRepository, MailService mailService,
                       @Qualifier("messageSource") MessageSource messageSource) {
        this.userRepository = userRepository;
        this.mailService = mailService;
        this.messageSource = messageSource;
    }

    public SiteUser register(SiteUser user) {
        return userRepository.save(user);
    }


    public VerificationMessage generateActivationNeededMessage(String email) {
        return new VerificationMessage(VerificationEvent.REGISTER, VerificationMessage.Type.danger,
                email, messageSource.getMessage("user.activation.awaits", null, Locale.getDefault()));
    }

    public Optional<SiteUser> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean isEmailUnique(String email) {
        return !userRepository.findByEmail(email).isPresent();
    }
}
