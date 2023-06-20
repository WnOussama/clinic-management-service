package com.nexym.clinic.domain.user;

import com.nexym.clinic.domain.user.port.PasswordResetTokenPersistence;
import com.nexym.clinic.infra.user.entity.ResetPasswordEntity;
import org.springframework.beans.factory.annotation.Autowired;

public class PasswordResetTokenServiceImpl implements PasswordResetTokenService{

    @Autowired
    private PasswordResetTokenPersistence passwordResetTokenPersistence;


    @Override
    public ResetPasswordEntity findByToken(String token) {
        return passwordResetTokenPersistence.findByToken(token).orElse(null);
    }

    @Override
    public ResetPasswordEntity save(ResetPasswordEntity resetPasswordEntity) {
        return passwordResetTokenPersistence.save(resetPasswordEntity);
    }
}
