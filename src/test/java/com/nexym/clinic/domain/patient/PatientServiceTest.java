package com.nexym.clinic.domain.patient;

import com.nexym.clinic.domain.patient.exception.PatientNotFoundException;
import com.nexym.clinic.domain.patient.exception.PatientValidationException;
import com.nexym.clinic.domain.patient.model.Patient;
import com.nexym.clinic.domain.patient.model.PatientList;
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
import java.util.List;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
@Sql("/user/db/data-27-02-2023.sql")
class PatientServiceTest {

    @Autowired
    private PatientService patientService;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Test
    void should_update_patient_by_id_not_exist_fail() {
        // When
        ThrowableAssert.ThrowingCallable callable = () -> patientService.updatePatientById(3L, getPatient(Civility.MR,
                "Marshall",
                "Baba",
                "0223344311",
                "marshall.baba@mail.com",
                "Toto2022"));
        // Then
        Assertions.assertThatThrownBy(callable)
                .isInstanceOf(PatientNotFoundException.class)
                .hasMessage("Patient with id '3' does not exist");
    }

    @Test
    void should_delete_patient_by_id_not_exist_fail() {
        // When
        ThrowableAssert.ThrowingCallable callable = () -> patientService.deletePatientById(3L);
        // Then
        Assertions.assertThatThrownBy(callable)
                .isInstanceOf(PatientNotFoundException.class)
                .hasMessage("Patient with id '3' does not exist");
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
    void should_update_patient_success() {
        // Given
        var patient = getPatient(Civility.MR,
                "Ali",
                "Baba",
                "0223344311",
                "ali.baba@mail.com",
                "Toto2022");

        // When
        patientService.updatePatientById(1L, patient);
        var updatedPatient = patientService.getPatientById(1L);
        // Then
        Assertions.assertThat(updatedPatient.getId()).isEqualTo(patient.getId());
        Assertions.assertThat(updatedPatient.getEmail()).isEqualTo(patient.getEmail());
        Assertions.assertThat(updatedPatient.getFirstName()).isEqualTo(patient.getFirstName());
        Assertions.assertThat(updatedPatient.getLastName()).isEqualTo(patient.getLastName());
        Assertions.assertThat(updatedPatient.getPhoneNumber()).isEqualTo(patient.getPhoneNumber());
    }

    @Test
    void should_search_patients_success() {
        var patientList = patientService.getPatientList(0, 10);
        // Then
        var items = List.of(Patient.PatientBuilder()
                .id(1L)
                .userId(1L)
                .creationDate(LocalDateTime.parse("2023-02-21 10:50:54", formatter))
                .appointments(List.of())
                .build());
        Assertions.assertThat(patientList).isEqualTo(PatientList.builder()
                .items(items)
                .first(true)
                .last(true)
                .number(0)
                .totalElements(1L)
                .numberOfElements(1)
                .totalPages(1)
                .build());
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
