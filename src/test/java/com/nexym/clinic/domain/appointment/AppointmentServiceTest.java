package com.nexym.clinic.domain.appointment;

import com.nexym.clinic.domain.appointment.model.Appointment;
import com.nexym.clinic.domain.appointment.model.Status;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
@Sql("/user/db/data-27-02-2023.sql")
class AppointmentServiceTest {

    @Autowired
    private AppointmentService appointmentService;

    @Test
    void should_get_appointment_list_success() {
//        var appointmentList = appointmentService.getAppointmentList();
//        // Then
//        Assertions.assertThat(appointmentList).isEqualTo(List.of(getAppointment(
//                2L,
//                1L,
//                "Hey",
//                Status.PENDING)));
    }

    private static Appointment getAppointment(Long doctorId, Long patientId, String prescription, Status status) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return Appointment.builder()
                .id(1L)
                .prescription(prescription)
                .status(status)
                .appointmentDate(LocalDateTime.parse("2023-04-06 03:20:54", formatter))
                .creationDate(LocalDateTime.parse("2023-04-06 03:19:54", formatter))
                .build();
    }
}
