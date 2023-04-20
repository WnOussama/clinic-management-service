package com.nexym.clinic.domain.appointment;

import com.nexym.clinic.domain.appointment.model.Appointment;
import com.nexym.clinic.domain.appointment.port.AppointmentPersistence;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    private AppointmentPersistence appointmentPersistence;

    @Override
    public List<Appointment> getAppointmentList() {
        return appointmentPersistence.getAppointmentList();
    }
}
