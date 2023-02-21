package com.nexym.clinic.domain.user.port;

import com.nexym.clinic.domain.user.model.User;

import java.util.Optional;

public interface UserPersistence {

    Optional<User> getUserById(Long userId);

    Long registerUser(User user);
}
