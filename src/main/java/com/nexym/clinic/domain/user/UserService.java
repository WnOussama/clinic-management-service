package com.nexym.clinic.domain.user;

import com.nexym.clinic.domain.user.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    User getUserById(Long userId);

    Long registerUser(User user);

    List<User> getUserList();
}
