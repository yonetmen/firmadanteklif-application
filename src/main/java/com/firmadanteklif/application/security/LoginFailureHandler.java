package com.firmadanteklif.application.security;

import com.firmadanteklif.application.controller.exception.LoginFailureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

@Slf4j
@Component
public class LoginFailureHandler implements AuthenticationFailureHandler {

    private MessageSource messageSource;

    @Autowired
    public LoginFailureHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        String email = request.getParameter("email");
        log.error("User Login failed. Login attempt with: " + email);
        throw new LoginFailureException(
                messageSource.getMessage("email.password.not.match", null, Locale.getDefault()));
    }
}