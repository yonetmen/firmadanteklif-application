package com.firmadanteklif.application.controller;

import com.firmadanteklif.application.domain.dto.FlashMessage;
import com.firmadanteklif.application.domain.entity.City;
import com.firmadanteklif.application.domain.entity.SiteUser;
import com.firmadanteklif.application.domain.entity.VerificationCode;
import com.firmadanteklif.application.domain.enums.VerificationEvent;
import com.firmadanteklif.application.exception.UserNotFoundException;
import com.firmadanteklif.application.service.HomeService;
import com.firmadanteklif.application.service.MailService;
import com.firmadanteklif.application.service.UserService;
import com.firmadanteklif.application.service.VerificationService;
import com.firmadanteklif.application.utility.FlashUtility;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Controller
@AllArgsConstructor
public class UserController {

    private UserService userService;
    private VerificationService verificationService;
    private BCryptPasswordEncoder passwordEncoder;
    private MailService mailService;
    private MessageSource messageSource;
    private HomeService homeService;

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

    @PostMapping("/user-kayit")
    public String registerNewUser(@Valid @ModelAttribute("user") SiteUser user, BindingResult bindingResult,
                                  Model model, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("validationErrors", bindingResult.getAllErrors());
            return "user/register";
        } else if (!user.getPassword().equals(user.getConfirmPassword())) {
            bindingResult.rejectValue("password", "password.match.error");
            return "user/register";
        } else {
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);
            SiteUser newUser = userService.register(user);
            String codeForRegister = createVerificationCodeForEvent(newUser.getUuid(), VerificationEvent.REGISTER);
            log.info("Register Verification URL: localhost:8080/activation/"+ user.getEmail() + "/" + codeForRegister);
//            mailService.sendActivationEmail(newUser, codeForRegister);
            redirectAttributes
                    .addFlashAttribute("userEmail", newUser.getEmail())
                    .addFlashAttribute("userRegisterSuccess", true);
            return "redirect:/";
        }
    }

    @GetMapping("sifre-hatirlatma")
    public String getResetPasswordForm(Model model) {
        model.addAttribute("user", new SiteUser());
        return "user/password-reset";
    }

    @PostMapping("/sifre-hatirlatma")
    public String sendResetPasswordEmail(@ModelAttribute("user") SiteUser user, Model model) {

        Optional<SiteUser> optional = userService.findUserByEmail(user.getEmail());

        if(!optional.isPresent()) {
            log.error("No user found with given email: " + user.getEmail());
            throw new UserNotFoundException(user.getEmail());
        }

        SiteUser siteUser = optional.get();
        String verificationCodeId = createVerificationCodeForEvent(siteUser.getUuid(), VerificationEvent.FORGOT_PASSWORD);
        log.info("Password Reset URL: localhost:8080/reset-password/"+ user.getEmail() + "/" + verificationCodeId);
//        mailService.sendResetPasswordEmail(siteUser, verificationCodeId);
        FlashMessage flashMessage = FlashUtility.getFlashMessage(FlashUtility.FLASH_SUCCESS,
                messageSource.getMessage("user.password.reset.mail.sent", null, Locale.getDefault()));
        model.addAttribute("flashMessage", flashMessage);
        return "user/password-reset";
    }

    @PostMapping("/sifre-yenileme")
    public String updatePassword(@ModelAttribute("user") SiteUser user, Model model, BindingResult bindingResult) {

        if(user.getPassword().trim().length() < 6) {
            bindingResult.rejectValue("password", null, "Şifre alanı en az 6 karakter olmalıdır.");
            return "user/password-new";
        }
        if(user.getPassword().trim().length() > 50) {
            bindingResult.rejectValue("password", null, "Şifre alanı en fazla 99 karakter olmalıdır.");
            return "user/password-new";
        }
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            bindingResult.rejectValue("password", null,
                    messageSource.getMessage("password.match.error", null, Locale.getDefault()));
            return "user/password-new";
        }

        Optional<SiteUser> userOptional = userService.findUserByEmail(user.getEmail());
        SiteUser existingUser = userOptional.orElseThrow(() -> new UserNotFoundException(user.getEmail()));
        existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        SiteUser updateUser = userService.updateUser(existingUser);

        FlashMessage flashMessage = FlashUtility.getFlashMessage(FlashUtility.FLASH_SUCCESS,
                messageSource.getMessage("user.password.reset.success", null, Locale.getDefault()));
        model.addAttribute("flashMessage", flashMessage);
        model.addAttribute("user", updateUser);
        return "user/login";
    }

    @GetMapping("user-profile")
    public String getUserProfile(HttpServletRequest request, Model model) {
        String email = request.getRemoteUser();
        Optional<SiteUser> user = userService.findUserByEmail(email);
        List<City> cities = homeService.getAllCityNames();
        model.addAttribute("cities", cities);
        model.addAttribute("user", user.get());
        return "user/profile";
    }

    @PostMapping("/user-update")
    public String updateUser(@ModelAttribute("user") SiteUser user, Model model, BindingResult bindingResult) {

        if(user.getPassword().trim().length() < 6) {
            bindingResult.rejectValue("password", null, "Şifre alanı en az 6 karakter olmalıdır.");
            return "user/user-profile";
        }
        if(user.getPassword().trim().length() > 50) {
            bindingResult.rejectValue("password", null, "Şifre alanı en fazla 99 karakter olmalıdır.");
            return "user/user-profile";
        }
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            bindingResult.rejectValue("password", null,
                    messageSource.getMessage("password.match.error", null, Locale.getDefault()));
            return "user/user-profile";
        }

        Optional<SiteUser> userOptional = userService.findUserByEmail(user.getEmail());
        SiteUser existingUser = userOptional.orElseThrow(() -> new UserNotFoundException(user.getEmail()));
        existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        SiteUser updateUser = userService.updateUser(existingUser);

        FlashMessage flashMessage = FlashUtility.getFlashMessage(FlashUtility.FLASH_SUCCESS,
                messageSource.getMessage("user.password.reset.success", null, Locale.getDefault()));
        model.addAttribute("flashMessage", flashMessage);
        model.addAttribute("user", updateUser);
        return "user/login";

    }

    private String createVerificationCodeForEvent(UUID userId, VerificationEvent event) {
        VerificationCode recoverPassword = new VerificationCode();
        recoverPassword.setVerificationEvent(event);
        recoverPassword.setOwnerId(userId);
        recoverPassword.setExpirationDate(LocalDateTime.now().plusDays(2));
        UUID verificationID = verificationService.save(recoverPassword);
        return verificationID.toString();
    }
}
