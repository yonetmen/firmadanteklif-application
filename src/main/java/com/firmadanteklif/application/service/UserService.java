package com.firmadanteklif.application.service;

import com.firmadanteklif.application.entity.SiteUser;
import com.firmadanteklif.application.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;

@Service
public class UserService {

    private UserRepository userRepository;
    private MailService mailService;

    @Autowired
    public UserService(UserRepository userRepository, MailService mailService) {
        this.userRepository = userRepository;
        this.mailService = mailService;
    }


    public SiteUser register(SiteUser user) {
        return userRepository.save(user);
    }

    public void sendActivationEmail(SiteUser user) {
        mailService.sendActivationEmail(user);
    }
}
