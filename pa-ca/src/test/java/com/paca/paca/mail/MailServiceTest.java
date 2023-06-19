package com.paca.paca.mail;

import com.paca.paca.mail.service.MailService;
import com.paca.paca.exception.exceptions.IOException;
import com.paca.paca.exception.exceptions.MessagingException;

import io.github.cdimascio.dotenv.Dotenv;

import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class MailServiceTest {

    @InjectMocks
    private MailService mailService;

    @Mock
    private JavaMailSender mailSender;

    @BeforeEach
    public void setMailServiceProperties() {
        Dotenv dotenv = Dotenv.load();
        mailService.setEMAIL_FROM(dotenv.get("GOOGLE_EMAIL_FROM"));
    }

    @Test
    public void testPropertiesAreInitialized() {
        // Verify that the properties annotated with @Value in MailService are not null
        assertNotNull(mailService.getEMAIL_FROM());
    }

    @Test
    @Disabled
    public void testSendResetPasswordEmail() throws IOException, MessagingException {
    }

    @Test
    @Disabled
    void shouldSendResetPasswordEmailThrowsMessagingException() {
    }

}
