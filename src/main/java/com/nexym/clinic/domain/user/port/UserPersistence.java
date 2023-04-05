package com.nexym.clinic.domain.user.port;

import com.nexym.clinic.domain.user.model.User;
import com.nexym.clinic.infra.user.entity.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserPersistence {

    Optional<User> getUserById(Long userId);

    Optional<User> getUserByEmail(String email);

    Optional<UserEntity> findByResetToken(String resetToken);

    Long registerUser(User user);

    boolean existsByEmail(String userEmail);

    List<User> getUserList();

    User save(User user);

    void deleteById(Long userId);
}
