package com.nexym.clinic.infra.user.dao;

import com.nexym.clinic.infra.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends JpaRepository<UserEntity, Long> {

    boolean existsByEmail(String email);
}
