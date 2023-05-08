package com.nexym.clinic.domain.appointment;

import com.nexym.clinic.domain.appointment.exception.AppointmentValidationException;
import com.nexym.clinic.domain.appointment.model.Appointment;
import com.nexym.clinic.domain.availability.model.Availability;
import com.nexym.clinic.domain.doctor.exception.DoctorNotFoundException;
import com.nexym.clinic.domain.patient.exception.PatientNotFoundException;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;
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

    private final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Test
    void should_add_new_appointment_patient_not_found_fail() {
        // When
        ThrowableAssert.ThrowingCallable callable = () -> appointmentService.addNewAppointment(2L,
                1L,
                LocalDateTime.parse("2025-04-06 03:20:54", formatter));
        // Then
        Assertions.assertThatThrownBy(callable)
                .isInstanceOf(PatientNotFoundException.class)
                .hasMessage("Patient with id '2' does not exist");
    }

    @Test
    void should_add_new_appointment_doctor_not_found_fail() {
        // When
        ThrowableAssert.ThrowingCallable callable = () -> appointmentService.addNewAppointment(1L,
                52L,
                LocalDateTime.parse("2025-04-06 03:20:54", formatter));
        // Then
        Assertions.assertThatThrownBy(callable)
                .isInstanceOf(DoctorNotFoundException.class)
                .hasMessage("Doctor with id '52' does not exist");
    }

    @Test
    void should_add_new_appointment_appointment_date_not_in_working_day_hours_fail() {
        // When
        ThrowableAssert.ThrowingCallable callable = () -> appointmentService.addNewAppointment(1L,
                1L,
                LocalDateTime.parse("2025-04-06 03:20:54", formatter));
        // Then
        Assertions.assertThatThrownBy(callable)
                .isInstanceOf(AppointmentValidationException.class)
                .hasMessage("Requested appointment date '2025-04-06T03:20:54' does not respect rule hours");
    }

    @Test
    void should_add_new_appointment_appointment_date_in_break_time_hours_fail() {
        // When
        ThrowableAssert.ThrowingCallable callable = () -> appointmentService.addNewAppointment(1L,
                1L,
                LocalDateTime.parse("2025-04-06 13:20:54", formatter));
        // Then
        Assertions.assertThatThrownBy(callable)
                .isInstanceOf(AppointmentValidationException.class)
                .hasMessage("Requested appointment date '2025-04-06T13:20:54' does not respect rule hours");
    }

    @Test
    void should_add_new_appointment_appointment_date_in_working_time_hours_exceeding_speciality_duration_fail() {
        // When
        ThrowableAssert.ThrowingCallable callable = () -> appointmentService.addNewAppointment(1L,
                1L,
                LocalDateTime.parse("2023-04-06 17:50:54", formatter));
        // Then
        Assertions.assertThatThrownBy(callable)
                .isInstanceOf(AppointmentValidationException.class)
                .hasMessage("Requested appointment date '2023-04-06T17:50:54' does not respect rule hours");
    }

    @Test
    void should_add_new_appointment_missing_doctor_availabilities_fail() {
        // When
        ThrowableAssert.ThrowingCallable callable = () -> appointmentService.addNewAppointment(1L,
                2L,
                LocalDateTime.parse("2025-04-06 14:15:00", formatter));
        // Then
        Assertions.assertThatThrownBy(callable)
                .isInstanceOf(AppointmentValidationException.class)
                .hasMessage("We cannot find any availability for doctor with id '2'");
    }

    @Test
    void should_add_new_appointment_missing_matching_date_range_availability_fail() {
        // When
        ThrowableAssert.ThrowingCallable callable = () -> appointmentService.addNewAppointment(1L,
                1L,
                LocalDateTime.parse("2025-04-26 14:15:00", formatter));
        // Then
        Assertions.assertThatThrownBy(callable)
                .isInstanceOf(AppointmentValidationException.class)
                .hasMessage("Doctor with id '1' has any availability matching the appointment date '2025-04-26T14:15'");
    }

    @Test
    void should_find_appointments_by_doctor_id_not_found_fail() {
        // Given
        var doctorId = 523L;
        // When
        ThrowableAssert.ThrowingCallable callable = () -> appointmentService.getAppointmentByDoctorId(doctorId);
        // Then
        Assertions.assertThatThrownBy(callable)
                .isInstanceOf(DoctorNotFoundException.class)
                .hasMessage("Doctor with id '523' does not exist");
    }

    @Test
    void should_find_appointments_by_doctor_id_availability_not_found_fail() {
        // Given
        var doctorId = 2L;
        // When
        ThrowableAssert.ThrowingCallable callable = () -> appointmentService.getAppointmentByDoctorId(doctorId);
        // Then
        Assertions.assertThatThrownBy(callable)
                .isInstanceOf(AppointmentValidationException.class)
                .hasMessage("We cannot find any availability for doctor with id '2'");
    }

    @Test
    void should_find_appointments_by_doctor_id_success() {
        // Given
        var doctorId = 1L;
        // When
        var doctorAppointments = appointmentService.getAppointmentByDoctorId(doctorId);
        // Then
        Appointment expectedAppointment = Appointment.builder()
                .id(1L)
                .patientId(1L)
                .availability(Availability.builder()
                        .id(1L)
                        .startDate(LocalDateTime.parse("2023-04-24 09:00:00", formatter))
                        .endDate(LocalDateTime.parse("2023-04-24 18:00:00", formatter))
                        .build())
                .appointmentDate(LocalDateTime.parse("2023-04-24 09:00:00", formatter))
                .creationDate(LocalDateTime.parse("2023-04-23 20:55:54", formatter))
                .build();

        Appointment actualAppointment = doctorAppointments.get(0);

        Assertions.assertThat(actualAppointment).isEqualTo(expectedAppointment);
    }

}
