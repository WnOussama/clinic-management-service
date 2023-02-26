package com.nexym.clinic.domain.user.port;

import com.nexym.clinic.domain.user.model.User;

import java.util.Optional;

public interface UserPersistence {

    Optional<User> getUserById(Long userId);
    Optional<User> getUserByEmail(String email);

    Long registerUser(User user);

    boolean existsByEmail(String userEmail);
}
