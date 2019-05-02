package com.firmadanteklif.application.controller;

import com.firmadanteklif.application.controller.exception.EmailNotUniqueException;
import com.firmadanteklif.application.controller.exception.LoginFailureException;
import com.firmadanteklif.application.controller.exception.NotValidatedException;
import com.firmadanteklif.application.controller.exception.ValidationFailedException;
import com.firmadanteklif.application.domain.dto.FlashMessage;
import com.firmadanteklif.application.security.LoginFailureHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Slf4j
@EnableWebMvc
@ControllerAdvice
public class ErrorController {

    @ExceptionHandler(EmailNotUniqueException.class)
    public ModelAndView handleEmailNotUniqueException(EmailNotUniqueException exception) {
        log.info("Handle EmailNotUniqueException");
        return getModelAndView(exception.getMessage(), "user/register");
    }

    @ExceptionHandler(LoginFailureException.class)
    public ModelAndView handleLoginFailureException(LoginFailureException exception) {
        log.info("Handle LoginFailureException");
        return getModelAndView(exception.getMessage(), "user/login");
    }

    @ExceptionHandler(NotValidatedException.class)
    public ModelAndView handleNotValidatedException(NotValidatedException exception) {
        log.info("Handle NotValidatedException");
        return getModelAndView(exception.getMessage(), "user/login");
    }

    @ExceptionHandler(ValidationFailedException.class)
    public ModelAndView handleValidationFailedException(ValidationFailedException exception) {
        log.info("Handle ValidationFailedException");
        return getModelAndView(exception.getMessage(), "user/login");
    }

    private ModelAndView getModelAndView(String message, String viewName) {
        ModelAndView mav = new ModelAndView();
        FlashMessage flashMessage = new FlashMessage();
        flashMessage.setKind("danger");
        flashMessage.setMessage(message);
        mav.getModel().put("flashMessage", flashMessage);
        mav.setViewName(viewName);
        return mav;
    }
}