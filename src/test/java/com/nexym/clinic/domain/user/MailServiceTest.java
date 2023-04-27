package com.nexym.clinic.domain.user;

import com.nexym.clinic.domain.user.mail.MailDetail;
import com.nexym.clinic.domain.user.mail.MailServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
@Sql("/user/db/data-27-02-2023.sql")
class MailServiceTest {

    @InjectMocks
    private MailServiceImpl mailService;

    @Mock
    private JavaMailSender mailSender;

    @Test
    void test_send_mail_success() {
        // Arrange
        MailDetail mailDetail = new MailDetail();
        mailDetail.setRecipient("recipient@example.com");
        mailDetail.setSubject("Test Subject");
        mailDetail.setMsgBody("Test Message");

        // Act
        String result = mailService.sendMail(mailDetail);

        // Assert
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
        assertEquals("Email has been sent successfully...", result);
    }

    @Test
    void test_send_mail_failure() {
        // Arrange
        MailDetail mailDetail = new MailDetail();
        mailDetail.setRecipient("recipient@example.com");
        mailDetail.setSubject("Test Subject");
        mailDetail.setMsgBody("Test Message");

        // mock mailSender.send() method to throw an exception
        doThrow(new RuntimeException()).when(mailSender).send(any(SimpleMailMessage.class));

        // Act
        String result = mailService.sendMail(mailDetail);

        // Assert
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
        assertEquals("Error while Sending email!!!", result);
    }
}

