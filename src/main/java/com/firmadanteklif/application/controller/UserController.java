package com.firmadanteklif.application.controller;

import com.firmadanteklif.application.entity.SiteUser;
import com.firmadanteklif.application.entity.VerificationCode;
import com.firmadanteklif.application.entity.enums.VerificationEvent;
import com.firmadanteklif.application.service.UserService;
import com.firmadanteklif.application.service.VerificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
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
    private AuthenticationManager authenticationManager;

    @Autowired
    public UserController(UserService userService, BCryptPasswordEncoder passwordEncoder,
                          VerificationService verificationService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.verificationService = verificationService;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("user-giris")
    public String loginUser(Model model) {
        model.addAttribute("user", new SiteUser());
        return "user/login";
    }

//    @PostMapping("user-giris")
//    public String loginUser(@ModelAttribute("user") SiteUser user, Model model, HttpServletRequest req) {
//
//        SiteUser siteUser = userService.getUser(user);
//        log.info("Logged in user: " + siteUser);
//
//        if (siteUser == null) {
//            return "redirect:/user-giris?error=true";
//        }
//
//        if (!passwordEncoder.matches(user.getPassword(), siteUser.getPassword())) {
//            log.info("User entered Password is not matched with persisted password");
//            return "redirect:/user-giris?error=true";
//        }
//
//        if (!siteUser.isActive()) {
//            log.info("User not Activated:");
//            VerificationMessage verificationMessage = userService.generateActivationNeededMessage(user.getEmail());
//            model.addAttribute("user", user);
//            model.addAttribute("verificationMessage", verificationMessage);
//            return "user/login";
//        }
//        UsernamePasswordAuthenticationToken authReq
//                = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
//        Authentication auth = authenticationManager.authenticate(authReq);
//
//        SecurityContext sc = SecurityContextHolder.getContext();
//        sc.setAuthentication(auth);
//        HttpSession session = req.getSession(true);
//        session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, sc);
//        session.setAttribute("user", siteUser);
//        return "redirect:/user-profile";
//    }

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
        activation.setVerificationEvent(VerificationEvent.REGISTER);
        activation.setOwnerId(user.getUuid());
        activation.setExpirationDate(LocalDateTime.now().plusDays(1));
        UUID verificationID = verificationService.save(activation);
        return "localhost:8080/activation/" + user.getEmail() + "/" + verificationID.toString();
    }
}
