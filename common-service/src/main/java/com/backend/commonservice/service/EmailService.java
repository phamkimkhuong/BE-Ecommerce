package com.backend.commonservice.service;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Service
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class EmailService {
    JavaMailSender javaMailSender;
    Configuration configuration;

    public EmailService(JavaMailSender javaMailSender,
                        Configuration configuration) {
        this.javaMailSender = javaMailSender;
        this.configuration = configuration;
    }

    /**
     * Sends an email with the specified parameters.
     *
     * @param to          The recipient's email address.
     * @param subject     The subject of the email.
     * @param teampleName The name of the email template.
     * @param placeholder A map of placeholders to be replaced in the template.
     * @param attachment  An optional file attachment.
     */
    public void sendEmail(String to, String subject, String teampleName, Map<String, Object> placeholder, File attachment) {
        log.info("Sending email to: {}, subject: {}", to, subject);
        try {
            Template template = configuration.getTemplate(teampleName);
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, placeholder);
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(html, true); // Set to true for HTML content
            if (attachment != null) {
                FileSystemResource file = new FileSystemResource(attachment);
                helper.addAttachment(file.getFilename(), file);
            }
            javaMailSender.send(message);
            log.info("Email sent successfully to: {}", to);
        } catch (MessagingException | IOException | TemplateException e) {
            log.error("Failed to send email: to {}", to, e);
            // Handle the exception as needed(retry logic, save to DLQ, etc.)

        }
    }
}
