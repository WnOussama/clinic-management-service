package com.nexym.clinic.domain.user.mail;

public interface MailService {
    String sendMail(MailDetail mailDetail);

    String sendMailWithAttachment(MailDetail mailDetail);
}
