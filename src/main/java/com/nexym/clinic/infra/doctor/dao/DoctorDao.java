package com.nexym.clinic.infra.doctor.dao;

import com.nexym.clinic.infra.doctor.entity.DoctorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DoctorDao extends JpaRepository<DoctorEntity, Long> {

    Optional<DoctorEntity> findByUserEmail(String email);

}
