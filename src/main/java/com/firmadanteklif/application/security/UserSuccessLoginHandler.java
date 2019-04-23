package com.firmadanteklif.application.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Slf4j
@Component
public class UserSuccessLoginHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        log.info("AuthenticationSuccessHandler # onAuthenticationSuccess() function!");

        response.setStatus(HttpServletResponse.SC_OK);

        HttpSession session = request.getSession();

        if(session != null) {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            session.setAttribute("user", userPrincipal.getUser());
            response.sendRedirect("/user-profile");
        }
    }
}