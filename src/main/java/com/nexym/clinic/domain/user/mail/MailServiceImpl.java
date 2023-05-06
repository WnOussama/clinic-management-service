package com.nexym.clinic.domain.user.mail;

import com.nexym.clinic.utils.exception.TechnicalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class MailServiceImpl implements MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;

    @Override
    public void sendMail(MailDetail mailDetail) {
        try {
            // Creating a simple mail message object
            SimpleMailMessage emailMessage = new SimpleMailMessage();

            // Setting up necessary details of mail
            emailMessage.setFrom(senderEmail);
            emailMessage.setTo(mailDetail.getRecipient());
            emailMessage.setSubject(mailDetail.getSubject());
            emailMessage.setText(mailDetail.getMessageBody());

            // Sending the email
            mailSender.send(emailMessage);
        } catch (Exception e) {
            throw new TechnicalException("Error while Sending email", e);
        }
    }
}
