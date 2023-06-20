package com.nexym.clinic.domain.user.port;

import com.nexym.clinic.domain.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserPersistence {

    Optional<User> getUserById(Long userId);

    Optional<User> getUserByEmail(String email);

    Optional<User> getUserByToken(String token);

    List<User> getUserList();

    User save(User user);
}
