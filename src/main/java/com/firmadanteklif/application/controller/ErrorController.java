package com.firmadanteklif.application.controller;

import com.firmadanteklif.application.domain.dto.FlashMessage;
import com.firmadanteklif.application.domain.entity.SiteUser;
import com.firmadanteklif.application.exception.UserNotFoundException;
import com.firmadanteklif.application.utility.FlashUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@ControllerAdvice
public class ErrorController {

    @ExceptionHandler(UserNotFoundException.class)
    public ModelAndView handleEmailNotUniqueException(UserNotFoundException exception) {
        return getModelAndView(exception.getMessage(), "user/password-reset");
    }

    private ModelAndView getModelAndView(String message, String viewName) {
        ModelAndView mav = new ModelAndView();
        FlashMessage flashMessage = FlashUtility.getFlashMessage("danger", message);
        mav.getModel().put("flashMessage", flashMessage);
        mav.getModel().put("user", new SiteUser());
        mav.setViewName(viewName);
        return mav;
    }
}