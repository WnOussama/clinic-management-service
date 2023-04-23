package com.nexym.clinic.infra.appointment.repository;

import com.nexym.clinic.domain.appointment.model.Appointment;
import com.nexym.clinic.domain.appointment.port.AppointmentPersistence;
import com.nexym.clinic.infra.appointment.dao.AppointmentDao;
import com.nexym.clinic.infra.appointment.mapper.AppointmentEntityMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AppointmentRepository implements AppointmentPersistence {

    private final AppointmentEntityMapper appointmentEntityMapper;
    private final AppointmentDao appointmentDao;

    @Override
    public List<Appointment> getByAvailabilityId(Long availabilityId) {
        return appointmentEntityMapper.mapToModelList(appointmentDao.findByAvailabilityId(availabilityId));
    }

}
