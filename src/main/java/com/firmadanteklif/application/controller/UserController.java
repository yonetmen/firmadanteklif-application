package com.firmadanteklif.application.controller;

import com.firmadanteklif.application.entity.SiteUser;
import com.firmadanteklif.application.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Slf4j
@Controller
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("user-giris")
    public String loginUser(Model model) {
        model.addAttribute("user", new SiteUser());
        return "user/login";
    }

    @GetMapping("user-kayit")
    public String registerUser(Model model) {
        model.addAttribute("user", new SiteUser());
        return "user/register";
    }

    @GetMapping("user-profile")
    public String registerUser() {
        return "user/profile";
    }

    @PostMapping("/user-kayit")
    public String registerNewUser(@Valid @ModelAttribute("user") SiteUser user, BindingResult bindingResult,
                                  Model model, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            log.info("Validation errors were found while registering a new user");
            model.addAttribute("user", user);
            model.addAttribute("validationErrors", bindingResult.getAllErrors());
            if (!user.getPassword().equalsIgnoreCase(user.getConfirmPassword()))
                bindingResult.rejectValue("password", "password.match.error");
            return "user/register";
        } else if (!user.getPassword().equalsIgnoreCase(user.getConfirmPassword())) {
            bindingResult.rejectValue("password", "password.match.error");
            return "user/register";
        } else {
            log.info("New User registration: " + user);
            SiteUser newUser = userService.register(user);
            redirectAttributes
                    .addAttribute("id", newUser.getUuid())
                    .addFlashAttribute("success", true);
            return "redirect:/user-kayit";
        }
    }

//    @PostMapping("/user-giris")
//    public String loginUser(@ModelAttribute("user") SiteUser user, Model model, RedirectAttributes redirectAttributes) {
//
//        if (bindingResult.hasErrors()) {
//            log.info("Validation errors were found while registering a new user");
//            model.addAttribute("user", user);
//            model.addAttribute("validationErrors", bindingResult.getAllErrors());
//            if (!user.getPassword().equalsIgnoreCase(user.getConfirmPassword()))
//                bindingResult.rejectValue("password", "password.match.error");
//            return "user/register";
//        } else if (!user.getPassword().equalsIgnoreCase(user.getConfirmPassword())) {
//            bindingResult.rejectValue("password", "password.match.error");
//            return "user/register";
//        } else {
//            log.info("New User registration: " + user);
//            SiteUser newUser = userService.register(user);
//            redirectAttributes
//                    .addAttribute("id", newUser.getUuid())
//                    .addFlashAttribute("success", true);
//            return "redirect:/user-kayit";
//        }
//    }
}
