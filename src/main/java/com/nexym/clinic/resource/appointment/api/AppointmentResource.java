package com.nexym.clinic.resource.appointment.api;

import com.nexym.clinic.api.AppointmentsApi;
import com.nexym.clinic.api.model.Appointment;
import com.nexym.clinic.api.model.AppointmentRequest;
import com.nexym.clinic.domain.appointment.AppointmentService;
import com.nexym.clinic.resource.appointment.mapper.AppointmentWsMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
public class AppointmentResource implements AppointmentsApi {

    private final AppointmentWsMapper appointmentWsMapper;

    private final AppointmentService appointmentService;

    @Override
    public ResponseEntity<Void> addNewAppointment(Long patientId, AppointmentRequest appointmentRequest) {
        appointmentService.addNewAppointment(patientId, appointmentRequest.getDoctorId(), appointmentWsMapper.mapTime(appointmentRequest.getAppointmentDate()));
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<Appointment>> getAppointmentsByDoctorId(Long doctorId) {
        return ResponseEntity.ok(appointmentWsMapper.mapToList(appointmentService.getAppointmentByDoctorId(doctorId)));
    }
}
