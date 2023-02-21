package com.nexym.clinic.domain.user;

import com.nexym.clinic.domain.user.model.User;

public interface UserService {
    User getUserById(Long userId);

    Long registerUser(User user);
}
