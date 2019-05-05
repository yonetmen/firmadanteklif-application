package com.firmadanteklif.application.config;

import com.firmadanteklif.application.domain.entity.SiteUser;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {

        if(SecurityContextHolder.getContext().getAuthentication() == null ||
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal().equals("anonymousUser")) {

            return Optional.of("master@firmadanteklif.com");
        }

        return Optional.of(((SiteUser) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal())
                .getEmail());
    }
}
