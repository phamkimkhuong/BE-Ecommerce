/*
 * @(#) $(NAME).java    1.0     5/14/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.backend.services.impl;

/*
 * @description
 * @author: Tran Tan Dat
 * @version: 1.0
 * @created: 14-May-2025 8:53 PM
 */

import com.backend.services.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private JavaMailSender emailSender;

    @Autowired
    public EmailServiceImpl(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    @Override
    public void sendMessage(String from, String to, String subject, String text) {
        // MimeMailMessage => có đính kèm media
        // SimpleMailMessage => nội dung thông thường
        MimeMessage message = emailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text,true);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        // thực hiện hành động gửi email
        emailSender.send(message);
    }
}
