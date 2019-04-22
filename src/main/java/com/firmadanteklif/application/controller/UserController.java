package com.firmadanteklif.application.controller;

import com.firmadanteklif.application.entity.SiteUser;
import com.firmadanteklif.application.entity.VerificationCode;
import com.firmadanteklif.application.entity.enums.VerificationType;
import com.firmadanteklif.application.repository.VerificationRepository;
import com.firmadanteklif.application.service.UserService;
import com.firmadanteklif.application.service.VerificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Controller
public class UserController {

    private UserService userService;
    private VerificationService verificationService;
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserService userService, BCryptPasswordEncoder passwordEncoder,
                          VerificationService verificationService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.verificationService = verificationService;
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
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);
            SiteUser newUser = userService.register(user);
            log.info("New User registration: " + newUser);
            String activationURL = sendActivationEmail(newUser); // No need to return this String in production.
            log.debug("ACTIVATION URL: " + activationURL);
            redirectAttributes
                    .addFlashAttribute("userEmail", newUser.getEmail())
                    .addFlashAttribute("userRegisterSuccess", true);
            return "redirect:/";
        }
    }

    // Returning activation URL. Implement Send Email in Production
    private String sendActivationEmail(SiteUser user) {
        VerificationCode activation = new VerificationCode();
        activation.setVerificationType(VerificationType.REGISTER);
        activation.setOwnerId(user.getUuid());
        activation.setExpirationDate(LocalDateTime.now().plusDays(1));
        UUID verificationID = verificationService.save(activation);
        return "localhost:8090/activation/" + verificationID.toString();
    }
}
