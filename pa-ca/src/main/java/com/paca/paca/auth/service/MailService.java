package com.paca.paca.auth.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


@Service
public class MailService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendResetPasswordEmail(String userEmail, String subject, String token) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        String htmlMsg = "<a href=\"http://localhost:8080/api/v1/auth/reset-password/" + token + "\">Click here</a>";
        try {
            helper.setFrom("info@paca.app");
            helper.setTo(userEmail);
            helper.setSubject(subject);
            helper.setText(htmlMsg, true);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        mailSender.send(mimeMessage);
    }
}