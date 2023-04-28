package com.nexym.clinic.infra.patient.dao;

import com.nexym.clinic.infra.patient.entity.PatientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientDao extends JpaRepository<PatientEntity, Long> {

    Optional<PatientEntity> findByUserEmail(String email);

}
