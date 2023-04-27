package com.nexym.clinic.domain.user.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Objects;

@Service
public class MailServiceImpl implements MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("$spring.mail.username")
    private String sender;

    @Override
    public String sendMail(MailDetail mailDetail) {
        // Try block to check for exceptions handling
        try {

            // Creating a simple mail message object
            SimpleMailMessage emailMessage = new SimpleMailMessage();

            // Setting up necessary details of mail
            emailMessage.setFrom(sender);
            emailMessage.setTo(mailDetail.getRecipient());
            emailMessage.setSubject(mailDetail.getSubject());
            emailMessage.setText(mailDetail.getMsgBody());

            // Sending the email
            mailSender.send(emailMessage);
            return "Email has been sent successfully...";
        }

        // Catch block to handle the exceptions
        catch (Exception e) {
            return "Error while Sending email!!!";
        }
    }

    @Override
    public String sendMailWithAttachment(MailDetail mailDetail) {
        // Creating a Mime Message
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;

        try {

            // Setting multipart as true for attachment to be sent
            mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setTo(mailDetail.getRecipient());
            mimeMessageHelper.setSubject(mailDetail.getSubject());
            mimeMessageHelper.setText(mailDetail.getMsgBody());

            // Adding the file attachment
            FileSystemResource file = new FileSystemResource(new File(mailDetail.getAttachment()));

            mimeMessageHelper.addAttachment(Objects.requireNonNull(file.getFilename()), file);

            // Sending the email with attachment
            mailSender.send(mimeMessage);
            return "Email has been sent successfully...";
        }

        // Catch block to handle the MessagingException
        catch (MessagingException e) {

            // Display message when exception is occurred
            return "Error while sending email!!!";
        }
    }


}
