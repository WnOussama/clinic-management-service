package com.nexym.clinic.infra.availability.dao;

import com.nexym.clinic.infra.availability.entity.AvailabilityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvailabilityDao extends JpaRepository<AvailabilityEntity, Long> {

}
