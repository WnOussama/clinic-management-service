package com.nexym.clinic.infra.appointment.repository;

import com.nexym.clinic.domain.appointment.model.Appointment;
import com.nexym.clinic.domain.appointment.port.AppointmentPersistence;
import com.nexym.clinic.infra.appointment.dao.AppointmentDao;
import com.nexym.clinic.infra.appointment.mapper.AppointmentEntityMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AppointmentRepository implements AppointmentPersistence {

    private final AppointmentEntityMapper appointmentEntityMapper;
    private final AppointmentDao appointmentDao;

    @Override
    public List<Appointment> getByAvailabilityId(Long availabilityId) {
        return appointmentEntityMapper.mapToModelList(appointmentDao.findByAvailabilityId(availabilityId));
    }

    @Override
    public Optional<Appointment> getByAppointmentIdAndDoctorId(Long appointmentId, Long doctorId) {
        return appointmentDao.findByIdAndAvailabilityDoctorId(appointmentId, doctorId).map(appointmentEntityMapper::mapToModel);
    }

    @Override
    public Long save(Appointment appointment) {
        var savedAppointment = appointmentDao.save(appointmentEntityMapper.mapToEntity(appointment));
        return savedAppointment.getId();
    }

}
