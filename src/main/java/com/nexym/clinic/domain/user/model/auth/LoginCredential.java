package com.nexym.clinic.domain.user.model.auth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginCredential {

    String email;
    String password;
}
