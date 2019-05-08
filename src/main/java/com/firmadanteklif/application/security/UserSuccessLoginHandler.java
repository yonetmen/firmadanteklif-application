package com.firmadanteklif.application.security;

import com.firmadanteklif.application.domain.dto.VerificationMessage;
import com.firmadanteklif.application.domain.entity.SiteUser;
import com.firmadanteklif.application.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Slf4j
@Component
public class UserSuccessLoginHandler implements AuthenticationSuccessHandler {

    private UserService userService;

    public UserSuccessLoginHandler(UserService userService) {
        this.userService = userService;
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
                VerificationMessage verificationMessage = userService.generateActivationNeededMessage(siteUser.getEmail());
                session.setAttribute("verificationMessage", verificationMessage);
                response.sendRedirect("/user-giris");
            }
        }
    }
}