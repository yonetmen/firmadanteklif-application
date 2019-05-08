package com.firmadanteklif.application.controller;

import com.firmadanteklif.application.domain.dto.FlashMessage;
import com.firmadanteklif.application.domain.entity.SiteUser;
import com.firmadanteklif.application.domain.entity.VerificationCode;
import com.firmadanteklif.application.domain.enums.VerificationEvent;
import com.firmadanteklif.application.exception.UserNotFoundException;
import com.firmadanteklif.application.service.MailService;
import com.firmadanteklif.application.service.UserService;
import com.firmadanteklif.application.service.VerificationService;
import com.firmadanteklif.application.utility.FlashUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
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
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Controller
public class UserController {

    private UserService userService;
    private VerificationService verificationService;
    private BCryptPasswordEncoder passwordEncoder;
    private MailService mailService;
    private MessageSource messageSource;

    @Autowired
    public UserController(UserService userService,
                          BCryptPasswordEncoder passwordEncoder,
                          VerificationService verificationService,
                          MailService mailService,
                          MessageSource messageSource) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.verificationService = verificationService;
        this.mailService = mailService;
        this.messageSource = messageSource;
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
            model.addAttribute("validationErrors", bindingResult.getAllErrors());
            return "user/register";
        } else if (!user.getPassword().equalsIgnoreCase(user.getConfirmPassword())) {
            bindingResult.rejectValue("password", "password.match.error");
            return "user/register";
        } else {
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);
            SiteUser newUser = userService.register(user);
            String codeForRegister = createVerificationCodeForRegister(newUser.getUuid());
            mailService.sendActivationEmail(newUser, codeForRegister);
            redirectAttributes
                    .addFlashAttribute("userEmail", newUser.getEmail())
                    .addFlashAttribute("userRegisterSuccess", true);
            return "redirect:/";
        }
    }

    @GetMapping("sifre-hatirlatma")
    public String passwordReminder(Model model) {
        model.addAttribute("user", new SiteUser());
        return "user/password-reset";
    }

    @PostMapping("/sifre-hatirlatma")
    public String passwordReminder(@ModelAttribute("user") SiteUser user, Model model) {

        Optional<SiteUser> optional = userService.findUserByEmail(user.getEmail());

        if(!optional.isPresent()) {
            log.error("No user found with given email: " + user.getEmail());
            throw new UserNotFoundException(user.getEmail());
        }

        SiteUser siteUser = optional.get();
        String verificationCodeId = createVerificationCodeForRecoverPassword(siteUser.getUuid());
        // Todo: Create password reset mail template.
//        mailService.sendResetPasswordEmail(siteUser, verificationCodeId);
        FlashMessage flashMessage = FlashUtility.getFlashMessage(FlashUtility.FLASH_SUCCESS,
                messageSource.getMessage("user.password.reset.mail.sent", null, Locale.getDefault()));
        model.addAttribute("flashMessage", flashMessage);
        return "user/password-reset";
    }

    private String createVerificationCodeForRecoverPassword(UUID userId) {
        VerificationCode recoverPassword = new VerificationCode();
        recoverPassword.setVerificationEvent(VerificationEvent.FORGOT_PASSWORD);
        recoverPassword.setOwnerId(userId);
        recoverPassword.setExpirationDate(LocalDateTime.now().plusDays(2));
        UUID verificationID = verificationService.save(recoverPassword);
        return verificationID.toString();
    }

    private String createVerificationCodeForRegister(UUID userId) {
        VerificationCode activation = new VerificationCode();
        activation.setVerificationEvent(VerificationEvent.REGISTER);
        activation.setOwnerId(userId);
        activation.setExpirationDate(LocalDateTime.now().plusDays(2));
        UUID verificationID = verificationService.save(activation);
        return verificationID.toString();
    }
}
