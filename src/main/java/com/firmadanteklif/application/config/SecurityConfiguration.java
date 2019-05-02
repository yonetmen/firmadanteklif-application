package com.firmadanteklif.application.config;

import com.firmadanteklif.application.security.LoginFailureHandler;
import com.firmadanteklif.application.security.LoginSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private UserDetailsService userDetailsService;
    private LoginSuccessHandler successLoginHandler;
    private LoginFailureHandler loginFailureHandler;

    @Autowired
    public SecurityConfiguration(@Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService,
                                 LoginSuccessHandler successLoginHandler, LoginFailureHandler loginFailureHandler) {
        this.userDetailsService = userDetailsService;
        this.successLoginHandler = successLoginHandler;
        this.loginFailureHandler = loginFailureHandler;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/",
                        "/css/**",
                        "/img/**",
                        "/user-giris",
                        "/user-kayit",
                        "/firma-giris",
                        "/firma-kayit",
                        "/activation/**").permitAll()
                .anyRequest()
                    .authenticated()
                .antMatchers("/user-profile")
                    .hasRole("USER")
                .antMatchers("/new-post")
                    .hasRole("USER")
                .and()
                .formLogin()
                    .loginPage("/user-giris")
                    .usernameParameter("email")
                    .loginProcessingUrl("/user-giris")
                    .successHandler(successLoginHandler)
//                    .failureUrl("/user-giris?error=true")
                    .failureHandler(loginFailureHandler)
                .and()
                .logout()
                    .logoutSuccessUrl("/")
                    .deleteCookies("JSESSIONID")
                .and()
                .rememberMe()
                    .key("cekoslavakyalilastiramadiklarimizdanmisiniz")
                    .tokenValiditySeconds(604800) // 1 week
                .and()
                    .csrf().disable()
                    .headers().frameOptions().disable();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
