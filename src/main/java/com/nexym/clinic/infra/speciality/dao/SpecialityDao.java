package com.nexym.clinic.infra.speciality.dao;

import com.nexym.clinic.infra.speciality.entity.SpecialityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecialityDao extends JpaRepository<SpecialityEntity, Long> {
}
