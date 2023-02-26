package com.nexym.clinic.domain.user.model.auth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Authentication {

    private String token;
    private Long expiresIn;
}
