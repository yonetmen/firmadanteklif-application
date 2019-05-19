package com.firmadanteklif.application.security;

import com.firmadanteklif.application.domain.dto.FlashMessage;
import com.firmadanteklif.application.domain.entity.SiteUser;
import com.firmadanteklif.application.utility.FlashUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Locale;

@Slf4j
@Component
public class UserSuccessLoginHandler implements AuthenticationSuccessHandler {

    private MessageSource messageSource;

    @Autowired
    public UserSuccessLoginHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        HttpSession session = request.getSession();

        if(session != null) {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            SiteUser siteUser = userPrincipal.getUser();
            if(siteUser.isActive()) {
                log.info("User successfully logged in: " + siteUser.getEmail());
                response.setStatus(HttpServletResponse.SC_OK);
                session.setAttribute("user", siteUser);
                response.sendRedirect("/user-profile");
            } else {
                log.info("User is neither registered nor validated: " + siteUser.getEmail());
                // todo: implement re-send verification mail in case user lost or didn't receive the first mail.
                authentication.setAuthenticated(false);
                FlashMessage flashMessage = FlashUtility.getFlashMessage(FlashUtility.FLASH_DANGER,
                        messageSource.getMessage("user.activation.awaits", null, Locale.getDefault()));
                session.setAttribute("flashMessage", flashMessage);
                response.sendRedirect("/user-giris");
            }
        }
    }
}