package com.nexym.clinic.infra.appointment.dao;

import com.nexym.clinic.infra.appointment.entity.AppointmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentDao extends JpaRepository<AppointmentEntity, Long> {
    List<AppointmentEntity> findByAvailabilityId(Long availabilityId);

    Optional<AppointmentEntity> findByIdAndAvailabilityDoctorId(Long appointmentId, Long doctorId);
}
