package com.nexym.clinic.domain.user.model.auth;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@Builder
public class Authentication {

    private Long id;
    private boolean isDoctor;
    private String token;
    private Long expiresIn;
}
