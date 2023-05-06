package com.nexym.clinic.domain.user;

import com.nexym.clinic.domain.user.mail.MailDetail;
import com.nexym.clinic.domain.user.mail.MailServiceImpl;
import com.nexym.clinic.utils.exception.TechnicalException;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MailServiceTest {

    @InjectMocks
    private MailServiceImpl mailService;

    @Mock
    private JavaMailSender mailSender;

    @Test
    void test_send_mail_success() {
        // Arrange
        String subject = "Test Subject";
        String recipient = "recipient@example.com";
        String testMessage = "Test Message";
        var mailDetail = MailDetail.builder()
                .recipient(recipient)
                .subject(subject)
                .messageBody(testMessage)
                .build();

        // Act
        mailService.sendMail(mailDetail);

        // Assert
        var mailMessageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender, times(1)).send(mailMessageCaptor.capture());


        var expectedMessage = new SimpleMailMessage();
        expectedMessage.setSubject(subject);
        expectedMessage.setTo(recipient);
        expectedMessage.setText(testMessage);
        //TODO 06/05/2023 fix test application properties loading
//        expectedMessage.setFrom("healthystepsclinic1@gmail.com");
        Assertions.assertThat(mailMessageCaptor.getValue()).isEqualTo(expectedMessage);
    }

    @Test
    void test_send_mail_failure() {
        // Arrange
        MailDetail mailDetail = new MailDetail();
        mailDetail.setRecipient("recipient@example.com");
        mailDetail.setSubject("Test Subject");
        mailDetail.setMessageBody("Test Message");

        // mock mailSender.send() method to throw an exception
        doThrow(new RuntimeException()).when(mailSender).send(any(SimpleMailMessage.class));

        // Act
        ThrowableAssert.ThrowingCallable callable = () -> mailService.sendMail(mailDetail);

        // Assert
        Assertions.assertThatThrownBy(callable)
                .isInstanceOf(TechnicalException.class)
                .hasMessageContaining("Error while Sending email");
    }
}

