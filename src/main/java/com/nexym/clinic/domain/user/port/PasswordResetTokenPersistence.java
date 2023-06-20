package com.nexym.clinic.domain.user.port;

import com.nexym.clinic.infra.user.entity.ResetPasswordEntity;

import java.util.Optional;

public interface PasswordResetTokenPersistence {
    Optional<ResetPasswordEntity> findByToken(String token);

    ResetPasswordEntity save(ResetPasswordEntity resetPasswordEntity);
}
