package com.firmadanteklif.application.service;

import com.firmadanteklif.application.domain.entity.SiteUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.internet.MimeMessage;
import java.util.Locale;

@Service
public class MailService {

    private static final Logger log = LoggerFactory.getLogger(MailService.class);
    private static final String BASE_ACTIVATION_URL = "http://localhost:8080/activation/";
    private final SpringTemplateEngine templateEngine;
    private final JavaMailSender javaMailSender;
    private final String logoFileName = "email-logo";

    @Autowired
    public MailService(@Qualifier("getJavaMailSender") JavaMailSender javaMailSender,
                       @Qualifier("templateEngine") SpringTemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

    @Async
    public void sendEmailFromTemplate(SiteUser user, String templateName, String subject, String verificationCode) {
        Locale locale = Locale.getDefault();
        Context context = new Context(locale);
        context.setVariable("user", user);
        context.setVariable("logo", logoFileName);
        context.setVariable("verificationCode", verificationCode);
        context.setVariable("baseURL", BASE_ACTIVATION_URL);
        String content = templateEngine.process(templateName, context);
        sendEmail(user.getEmail(), subject, content,true,true);
    }

    @Async
    public void sendEmail(String to, String subject, String content, boolean isMultiPart, boolean isHtml) {
        log.debug("Sending Email");
        MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultiPart, "UTF-8");
            message.setTo(to);
            message.setFrom("kasimgul@gmail.com");
            message.setSubject(subject);
            message.setText(content, isHtml);
            message.addInline(logoFileName, new ClassPathResource("static/img/email-logo.png"), "image/png");
            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            log.error("Email could not be sent to user '{}': {}", to, e);
        }
    }

    @Async
    public void sendActivationEmail(SiteUser user, String verificationCode) {
        log.debug("Sending activation email to '{}'", user.getEmail());
        sendEmailFromTemplate(user, "email/activation",
                "Firmadan Teklif Uyelik Onayi", verificationCode);
    }

    @Async
    public void sendResetPasswordEmail(SiteUser user, String verificationCode) {
        log.debug("Sending activation email to '{}'", user.getEmail());
        sendEmailFromTemplate(user, "email/activation",
                "Firmadan Teklif Uyelik Onayi", verificationCode);
    }

    @Async
    public void sendWelcomeEmail(SiteUser user) {
        log.debug("Sending welcome email to '{}'", user.getEmail());
        sendEmailFromTemplate(user, "email/welcome",
                "FirmadanTeklif'e hosgeldiniz!", null);
    }
}
