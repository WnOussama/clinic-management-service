package com.nexym.clinic.domain.user.mail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MailDetail {

    private String recipient;
    private String subject;
    private String messageBody;
    private String attachment;

}
