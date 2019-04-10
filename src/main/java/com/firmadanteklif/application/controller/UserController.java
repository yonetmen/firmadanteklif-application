package com.firmadanteklif.application.controller;

import com.firmadanteklif.application.entity.SiteUser;
import com.firmadanteklif.application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("user-login")
    public String loginUser(Model model) {
        model.addAttribute("loginUser", new SiteUser());
        return "user/login";
    }

    @GetMapping("user-register")
    public String registerUser(Model model) {
        model.addAttribute("registerUser", new SiteUser());
        return "user/register";
    }
}
