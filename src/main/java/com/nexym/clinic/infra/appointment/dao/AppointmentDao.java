package com.nexym.clinic.infra.appointment.dao;

import com.nexym.clinic.infra.appointment.entity.AppointmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentDao extends JpaRepository<AppointmentEntity, Long> {
}
