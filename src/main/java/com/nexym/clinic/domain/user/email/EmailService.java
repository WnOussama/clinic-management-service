package com.nexym.clinic.domain.user.email;

import org.springframework.mail.SimpleMailMessage;


public interface EmailService {
    void sendEmail(SimpleMailMessage email);
}
