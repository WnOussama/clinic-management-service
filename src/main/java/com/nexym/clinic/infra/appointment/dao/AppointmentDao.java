package com.nexym.clinic.infra.appointment.dao;

import com.nexym.clinic.infra.appointment.entity.AppointmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentDao extends JpaRepository<AppointmentEntity, Long> {
    List<AppointmentEntity> findByAvailabilityId(Long availabilityId);
}
