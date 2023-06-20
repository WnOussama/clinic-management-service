package com.nexym.clinic.domain.user;

import com.nexym.clinic.domain.user.model.User;
import com.nexym.clinic.domain.user.model.auth.Authentication;
import com.nexym.clinic.domain.user.model.auth.LoginCredential;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    User getUserById(Long userId);

    List<User> getUserList();

    Authentication authenticate(LoginCredential loginCredential, AuthenticationManager authenticationManager);

    void forgetPassword(String email);

    void updatePassword(String token, String password);

    void confirmPasswordReset(String resetPasswordToken);
}
