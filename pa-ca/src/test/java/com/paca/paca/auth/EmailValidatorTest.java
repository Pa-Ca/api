package com.paca.paca.auth;

import com.paca.paca.auth.utils.EmailValidator;
import com.paca.paca.exception.exceptions.BadRequestException;
import com.paca.paca.exception.exceptions.ConflictException;
import com.paca.paca.exception.exceptions.UnprocessableException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmailValidatorTest {

    @Test
    @Disabled
    void shouldCheckThatEmailIsValid() {
        /*String email = mock(String.class);
        MockedStatic<EmailValidator> ev = mockStatic(EmailValidator.class);

        ev.validate(email);
        verify(ev, times(1)).validate(email);

        try {
        } catch (Exception e) {
            assertThat(e instanceof BadRequestException).isFalse();
            assertThat(e instanceof UnprocessableException).isFalse();
            assertThat(e instanceof ConflictException).isFalse();
        }*/

    }
}
