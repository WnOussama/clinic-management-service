package com.nexym.clinic.resource.appointment.api;

import com.nexym.clinic.api.AppointmentsApi;
import com.nexym.clinic.domain.appointment.AppointmentService;
import com.nexym.clinic.resource.appointment.mapper.AppointmentWsMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@AllArgsConstructor
public class AppointmentResource implements AppointmentsApi {

    private final AppointmentWsMapper appointmentWsMapper;

    private final AppointmentService appointmentService;

}
