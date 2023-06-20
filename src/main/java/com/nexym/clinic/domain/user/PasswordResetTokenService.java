package com.nexym.clinic.domain.user;

import com.nexym.clinic.infra.user.entity.ResetPasswordEntity;

public interface PasswordResetTokenService {
    ResetPasswordEntity findByToken(String token);
    ResetPasswordEntity save(ResetPasswordEntity resetPasswordEntity);
}
