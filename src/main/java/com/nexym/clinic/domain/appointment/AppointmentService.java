package com.nexym.clinic.domain.appointment;

import java.time.LocalDateTime;

public interface AppointmentService {

    void addNewAppointment(Long patientId, Long doctorId, LocalDateTime appointmentDate);
}
