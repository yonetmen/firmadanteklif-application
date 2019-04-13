package com.firmadanteklif.application.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/", "/static/**", "/user/login.html", "/user/register.html").permitAll()
                .antMatchers("/user/profile.html").hasRole("USER")
                .and().formLogin().loginPage("/user/login.html")
                .usernameParameter("email")
                .loginProcessingUrl("/user-giris")
                .defaultSuccessUrl("/user-profile", true)
                .failureUrl("/user/login.html?error=true")
                .and().logout()
                .and().rememberMe()
                .and().csrf().disable()
                .headers().frameOptions().disable();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .inMemoryAuthentication()
            .withUser("kasimgul@gmail.com")
            .password(passwordEncoder().encode("ksm123")).roles("USER")
            .and()
            .withUser("admin@gmail.com")
            .password(passwordEncoder().encode("ksm123")).roles("ADMIN");
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
