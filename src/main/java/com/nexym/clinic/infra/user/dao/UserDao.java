package com.nexym.clinic.infra.user.dao;

import com.nexym.clinic.domain.user.model.User;
import com.nexym.clinic.infra.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserDao extends JpaRepository<UserEntity, Long> {

    boolean existsByEmail(String email);

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> save(User user);

    Optional<UserEntity> findByResetToken(UUID token);
}
