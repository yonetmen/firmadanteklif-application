package com.firmadanteklif.application.service;

import com.firmadanteklif.application.entity.SiteUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final String BASE_URL = "http://localhost:8080";
    private final SpringTemplateEngine templateEngine;
    private final JavaMailSender javaMailSender;

    public MailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = new SpringTemplateEngine();
    }

    @Async
    public void sendEmail(String to, String subject, String content, boolean isMultiPart, boolean isHtml) {
        log.debug("Sending Email");
        MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "UTF-8");
            message.setTo(to);
            message.setFrom("noreply@firmadanteklif.com");
            message.setSubject(subject);
            message.setText(content,isHtml);
            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            log.error("Email could not be sent to user '{}': {}", to, e);
        }
    }

    @Async
    public void sendEmailFromTemplate(SiteUser user, String templateName, String subject) {
        Locale locale = Locale.getDefault();
        Context context = new Context(locale);
        context.setVariable("user", user);
        context.setVariable("baseURL", BASE_URL);
        String content = templateEngine.process(templateName, context);
        sendEmail(user.getEmail(), subject, content,false,true);
    }

    @Async
    public void sendActivationEmail(SiteUser user) {
        log.debug("Sending activation email to '{}'", user.getEmail());
        sendEmailFromTemplate(user, "email/activation", "Firmadan Teklif Uyelik Onayi");
    }

    @Async
    public void sendWelcomeEmail(SiteUser user) {
        log.debug("Sending welcome email to '{}'", user.getEmail());
        sendEmailFromTemplate(user, "email/welcome", "FirmadanTeklif'e hosgeldiniz!");
    }
}
