package com.nexym.clinic.domain.appointment.port;

import com.nexym.clinic.domain.appointment.model.Appointment;

import java.util.List;

public interface AppointmentPersistence {

    List<Appointment> getByAvailabilityId(Long availabilityId);

    List<Appointment> getAppointmentList();
}
