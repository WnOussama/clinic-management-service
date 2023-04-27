package com.nexym.clinic.domain.user.mail;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MailDetail {

    private String recipient;
    private String subject;
    private String msgBody;
    private String attachment;
}
