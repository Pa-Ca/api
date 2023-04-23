package com.paca.paca.mail.service;

import java.util.Map;
import static java.util.Map.entry;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import com.paca.paca.mail.utils.MailUtils;
import com.paca.paca.mail.statics.MailStatics;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class MailService {
    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String emailFrom;

    public void sendResetPasswordEmail(String userEmail, String token, String username) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        String filePath = "/templates/resetPassword.html";
        Map<String, String> data = Map.ofEntries(
                entry(MailStatics.Keys.USERNAME, username),
                entry(MailStatics.Keys.LINK_URL, MailStatics.Content.RESET_PASSWORD_URL + token)
        );

        String htmlMsg = MailUtils.htmlToString(filePath, data);

        try {
            helper.setFrom(emailFrom);
            helper.setTo(userEmail);
            helper.setSubject(MailStatics.Content.RESET_PASSWORD_SUBJECT);
            helper.setText(htmlMsg, true);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        mailSender.send(mimeMessage);
    }

    public void sendVerifydEmail(String userEmail, String token, String username) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        String filePath = "/templates/verifyEmail.html";
        Map<String, String> data = Map.ofEntries(
                entry(MailStatics.Keys.USERNAME, username),
                entry(MailStatics.Keys.LINK_URL, MailStatics.Content.VERIFIED_EMAIL_URL + token)
        );

        String htmlMsg = MailUtils.htmlToString(filePath, data);

        try {
            helper.setFrom(emailFrom);
            helper.setTo(userEmail);
            helper.setSubject(MailStatics.Content.VERIFY_EMAIL_SUBJECT);
            helper.setText(htmlMsg, true);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        mailSender.send(mimeMessage);
    }
}