package com.firmadanteklif.application.security;

import com.firmadanteklif.application.controller.exception.NotValidatedException;
import com.firmadanteklif.application.domain.entity.SiteUser;
import com.firmadanteklif.application.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Locale;

@Slf4j
@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private MessageSource messageSource;

    public LoginSuccessHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        HttpSession session = request.getSession();

        if(session != null) {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            SiteUser siteUser = userPrincipal.getUser();
            if(siteUser.isActive()) {
                log.info("User is logged in: " + siteUser.getEmail());
                response.setStatus(HttpServletResponse.SC_OK);
                session.setAttribute("user", siteUser);
                response.sendRedirect("/user-profile");
            } else {
                log.info("Logged in user (" + siteUser.getEmail() + "), is not activated.");
                authentication.setAuthenticated(false);
                throw new NotValidatedException(
                        messageSource.getMessage("user.activation.awaits", null, Locale.getDefault()));
            }
        }
    }
}