package com.nexym.clinic.domain.user;

import com.nexym.clinic.domain.user.model.User;
import com.nexym.clinic.domain.user.model.auth.Authentication;
import com.nexym.clinic.domain.user.model.auth.LoginCredential;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.UUID;

public interface UserService extends UserDetailsService {
    User getUserById(Long userId);

    List<User> getUserList();

    Authentication authenticate(LoginCredential loginCredential, AuthenticationManager authenticationManager);

    void forgetPassword(String email);

    void updatePassword(UUID token, String password);

    void confirmPasswordReset(UUID resetPasswordToken);
}
