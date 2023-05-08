package com.nexym.clinic.domain.appointment;

import com.nexym.clinic.domain.appointment.model.Appointment;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentService {

    void addNewAppointment(Long patientId, Long doctorId, LocalDateTime appointmentDate);

    List<Appointment> getAppointmentByDoctorId(Long doctorId);
}
