package com.nexym.clinic.resource.appointment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexym.clinic.ClinicManagementServiceApplication;
import com.nexym.clinic.api.model.AppointmentRequest;
import com.nexym.clinic.config.ClinicManagementExceptionHandler;
import com.nexym.clinic.domain.appointment.AppointmentService;
import com.nexym.clinic.resource.appointment.api.AppointmentResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.OffsetDateTime;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = ClinicManagementServiceApplication.class, webEnvironment = RANDOM_PORT)
class AppointmentResourceTest {

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AppointmentService appointmentService;

    @Autowired
    private AppointmentResource appointmentResource;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(appointmentResource)
                .setControllerAdvice(new ClinicManagementExceptionHandler())
                .apply(SecurityMockMvcConfigurers.springSecurity(springSecurityFilterChain))
                .build();
    }

    @Test
    void should_add_new_appointment_not_authorized_fail() throws Exception {
        var appointmentRequest = new AppointmentRequest()
                .doctorId(1L)
                .appointmentDate(OffsetDateTime.now());
        mockMvc.perform(post(addNewAppointmentUri())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(appointmentRequest)))
                .andExpect(status().isUnauthorized());
    }

    private String addNewAppointmentUri() {
        return "/api/v1/patients/1/appointments";
    }
}
