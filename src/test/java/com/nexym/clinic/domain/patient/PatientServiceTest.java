package com.nexym.clinic.domain.patient;

import com.nexym.clinic.domain.patient.exception.PatientValidationException;
import com.nexym.clinic.domain.patient.model.Patient;
import com.nexym.clinic.domain.user.exception.UserValidationException;
import com.nexym.clinic.domain.user.model.Civility;
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
class PatientServiceTest {

    @Autowired
    private PatientService patientService;

    @Test
    void should_register_patient_success() {
        // Given
        var patient = getPatient(Civility.MRS,
                "Ali",
                "Baba",
                "0223344311",
                "ali.baba@mail.com",
                "Toto2022");

        // When
        var foundPatient = patientService.registerPatient(patient);
        // Then
        Assertions.assertThat(foundPatient).isEqualTo(1L);
    }

    @Test
    void should_register_patient_existing_same_email_fail() {
        // Given
        var patient = getPatient(Civility.MRS,
                "Ali",
                "Baba",
                "0223344311",
                "john.doe@mail.com",
                "Toto2022");

        // When
        ThrowableAssert.ThrowingCallable callable = () -> patientService.registerPatient(patient);
        // Then
        Assertions.assertThatThrownBy(callable)
                .isInstanceOf(UserValidationException.class)
                .hasMessage("User with email 'john.doe@mail.com' already exists");
    }

    @Test
    void should_register_patient_missing_required_attribute_fail() {
        // When
        ThrowableAssert.ThrowingCallable callable = () -> patientService.registerPatient(Patient.PatientBuilder().build());
        // Then
        Assertions.assertThatThrownBy(callable)
                .isInstanceOf(PatientValidationException.class)
                .hasMessage("Failed to validate patient request");
    }

    private static Patient getPatient(Civility civility, String firstName, String lastName, String phoneNumber, String email, String password) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return Patient.PatientBuilder()
                .id(1L)
                .userId(1L)
                .civility(civility)
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .phoneNumber(phoneNumber)
                .password(password)
                .creationDate(LocalDateTime.parse("2023-02-21 10:50:54", formatter))
                .build();
    }
}
