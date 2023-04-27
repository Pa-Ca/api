package com.paca.paca.mail;

import com.paca.paca.exception.exceptions.MessagingException;
import com.paca.paca.mail.service.MailService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.testcontainers.shaded.com.google.common.base.CharMatcher.any;

@ExtendWith(MockitoExtension.class)
@TestPropertySource(properties = {
        "spring.mail.username=example@example.com"
})
public class MailServiceTest {

    @InjectMocks
    private MailService mailService;

    @Mock
    private JavaMailSender mailSender;

    @Test
    @Disabled
    void shouldSendResetPasswordEmail() throws MessagingException {
        String userEmail = "test@example.com";
        String token = "testToken";
        String username = "testUsername";

        mailService.sendResetPasswordEmail(userEmail, token, username);

        verify(mailSender).send((MimeMessagePreparator) any());
    }

    @Test
    @Disabled
    void shouldSendResetPasswordEmailThrowsMessagingException() {
        String userEmail = "test@example.com";
        String token = "testToken";
        String username = "testUsername";

        doThrow(new MessagingException("Email configuration is not valid", 41))
                .when(mailSender).send((MimeMessagePreparator) any());

        assertThrows(MessagingException.class, () -> {
            mailService.sendResetPasswordEmail(userEmail, token, username);
        });
    }

}
