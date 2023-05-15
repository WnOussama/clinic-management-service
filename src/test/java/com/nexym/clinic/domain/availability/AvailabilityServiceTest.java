package com.nexym.clinic.domain.availability;

import com.nexym.clinic.domain.availability.model.Availability;
import com.nexym.clinic.domain.doctor.exception.DoctorValidationException;
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
class AvailabilityServiceTest {

    @Autowired
    private AvailabilityService availabilityService;

    private final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    @Test
    void should_add_new_availability_missing_start_date_fail() {
        // When
        ThrowableAssert.ThrowingCallable callable = () -> availabilityService.addNewAvailability(1L, Availability.builder()
                .endDate(LocalDateTime.parse("2023-04-06 03:20:54", formatter))
                .build());
        // Then
        Assertions.assertThatThrownBy(callable)
                .isInstanceOf(DoctorValidationException.class)
                .hasMessage("Failed to validate availability request");
    }

    @Test
    void should_add_new_availability_missing_end_date_fail() {
        // When
        ThrowableAssert.ThrowingCallable callable = () -> availabilityService.addNewAvailability(1L, Availability.builder()
                .startDate(LocalDateTime.parse("2023-04-06 03:20:54", formatter))
                .build());
        // Then
        Assertions.assertThatThrownBy(callable)
                .isInstanceOf(DoctorValidationException.class)
                .hasMessage("Failed to validate availability request");
    }

    @Test
    void should_add_new_availability_doctor_not_found_fail() {
        // When
        ThrowableAssert.ThrowingCallable callable = () -> availabilityService.addNewAvailability(1L, getAvailability("2023-04-06 03:20:00",
                "2023-04-06 03:25:00"));
        // Then
        Assertions.assertThatThrownBy(callable)
                .isInstanceOf(DoctorValidationException.class)
                .hasMessage("Availability start date '2023-04-06T03:20' is in the past");
    }

    @Test
    void should_add_new_availability_start_date_before_now_fail() {
        // When
        ThrowableAssert.ThrowingCallable callable = () -> availabilityService.addNewAvailability(1L, getAvailability("2023-04-06 03:20:00",
                "2025-04-06 03:25:00"));
        // Then
        Assertions.assertThatThrownBy(callable)
                .isInstanceOf(DoctorValidationException.class)
                .hasMessage("Availability start date '2023-04-06T03:20' is in the past");
    }

    @Test
    void should_add_new_availability_end_date_before_start_date_fail() {
        // When
        ThrowableAssert.ThrowingCallable callable = () -> availabilityService.addNewAvailability(1L, getAvailability("2025-04-06 03:20:00",
                "2025-04-06 02:20:00"));
        // Then
        Assertions.assertThatThrownBy(callable)
                .isInstanceOf(DoctorValidationException.class)
                .hasMessage("Availability end date '2025-04-06T03:20' is before the start date '2025-04-06T02:20'");
    }

    @Test
    void should_add_new_availability_start_date_not_in_working_day_hours_range_fail() {
        // When
        ThrowableAssert.ThrowingCallable callable = () -> availabilityService.addNewAvailability(1L, getAvailability("2025-04-06 03:20:00",
                "2025-04-07 03:25:00"));
        // Then
        Assertions.assertThatThrownBy(callable)
                .isInstanceOf(DoctorValidationException.class)
                .hasMessage("Availability start date '2025-04-06T03:20' and end date '2025-04-07T03:25' do not respect global rule");
    }

    @Test
    void should_add_new_availability_end_date_not_in_working_day_hours_range_fail() {
        // When
        ThrowableAssert.ThrowingCallable callable = () -> availabilityService.addNewAvailability(1L, getAvailability("2025-04-06 09:20:00",
                "2025-04-07 03:25:00"));
        // Then
        Assertions.assertThatThrownBy(callable)
                .isInstanceOf(DoctorValidationException.class)
                .hasMessage("Availability start date '2025-04-06T09:20' and end date '2025-04-07T03:25' do not respect global rule");
    }

    @Test
    void should_add_new_availability_start_date_is_in_break_time_hours_range_fail() {
        // When
        ThrowableAssert.ThrowingCallable callable = () -> availabilityService.addNewAvailability(1L, getAvailability("2025-04-06 12:20:00",
                "2025-04-07 14:25:00"));
        // Then
        Assertions.assertThatThrownBy(callable)
                .isInstanceOf(DoctorValidationException.class)
                .hasMessage("Availability start date '2025-04-06T12:20' and end date '2025-04-07T14:25' do not respect global rule");
    }

    @Test
    void should_add_new_availability_end_date_is_in_break_time_hours_range_fail() {
        // When
        ThrowableAssert.ThrowingCallable callable = () -> availabilityService.addNewAvailability(1L, getAvailability("2025-04-06 09:20:00",
                "2025-04-07 13:25:00"));
        // Then
        Assertions.assertThatThrownBy(callable)
                .isInstanceOf(DoctorValidationException.class)
                .hasMessage("Availability start date '2025-04-06T09:20' and end date '2025-04-07T13:25' do not respect global rule");
    }

    @Test
    void should_add_new_availability_includes_weekend_days_fail() {
        // When
        ThrowableAssert.ThrowingCallable callable = () -> availabilityService.addNewAvailability(1L, getAvailability("2024-05-12 09:00:00",
                "2024-05-16 18:00:00"));
        // Then
        Assertions.assertThatThrownBy(callable)
                .isInstanceOf(DoctorValidationException.class)
                .hasMessage("Availability start date '2024-05-12T09:00' and end date '2024-05-16T18:00' includes weekend days");
    }

    @Test
    void should_add_new_availability_dates_gap_inferior_than_duration_fail() {
        // When
        ThrowableAssert.ThrowingCallable callable = () -> availabilityService.addNewAvailability(1L, getAvailability("2025-04-06 09:20:00",
                "2025-04-06 09:25:00"));
        // Then
        Assertions.assertThatThrownBy(callable)
                .isInstanceOf(DoctorValidationException.class)
                .hasMessage("The availability date gap is not sufficient for an appointment duration '15'");
    }

    private static Availability getAvailability(String startDate, String endDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return Availability.builder()
                .id(1L)
                .startDate(LocalDateTime.parse(startDate, formatter))
                .endDate(LocalDateTime.parse(endDate, formatter))
                .build();
    }
}
