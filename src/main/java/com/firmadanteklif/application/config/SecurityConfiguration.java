package com.firmadanteklif.application.config;

import com.firmadanteklif.application.security.UserDetailsServiceImpl;
import com.firmadanteklif.application.security.UserSuccessLoginHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private UserDetailsServiceImpl userDetailsService;
    private UserSuccessLoginHandler successLoginHandler;

    @Autowired
    public SecurityConfiguration(UserDetailsServiceImpl userDetailsService, UserSuccessLoginHandler successLoginHandler) {
        this.userDetailsService = userDetailsService;
        this.successLoginHandler = successLoginHandler;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/", "/static/**", "/user-giris", "/user-kayit").permitAll()
                .anyRequest().authenticated()
                .antMatchers("/user-profile").hasRole("USER")
                .and().formLogin().loginPage("/user-giris")
                .usernameParameter("email")
                .loginProcessingUrl("/user-giris")
                .successHandler(successLoginHandler)
                .failureUrl("/user-giris?error=true")
                .and().logout().logoutSuccessUrl("/")
                .and().rememberMe()
                .and().csrf().disable()
                .headers().frameOptions().disable();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
