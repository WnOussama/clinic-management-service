package com.nexym.clinic.domain.appointment.port;

import com.nexym.clinic.domain.appointment.model.Appointment;

import java.util.List;
import java.util.Optional;

public interface AppointmentPersistence {

    List<Appointment> getByAvailabilityId(Long availabilityId);
    Optional<Appointment> getByAppointmentIdAndDoctorId(Long appointmentId, Long doctorId);
    Long save(Appointment appointment);
}
