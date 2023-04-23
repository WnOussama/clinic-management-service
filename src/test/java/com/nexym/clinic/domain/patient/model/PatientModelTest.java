package com.nexym.clinic.domain.patient.model;

import com.nexym.clinic.domain.user.model.Civility;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class PatientModelTest {

    @Test
    void should_patient_validate_missing_first_name_fail() {
        var patient = Patient.PatientBuilder()
                .phoneNumber("01122334455")
                .lastName("Doe")
                .civility(Civility.MRS)
                .email("john.doe@mail.com")
                .password("Toto2024")
                .build();
        Assertions.assertThat(patient.applyValidations())
                .isEqualTo(List.of("First name should be filled"));
    }

    @Test
    void should_patient_validate_missing_last_name_fail() {
        var patient = Patient.PatientBuilder()
                .phoneNumber("01122334455")
                .firstName("John")
                .civility(Civility.MRS)
                .email("john.doe@mail.com")
                .password("Toto2024")
                .build();
        Assertions.assertThat(patient.applyValidations())
                .isEqualTo(List.of("Last name should be filled"));
    }

    @Test
    void should_patient_validate_missing_all_required_attributes_fail() {
        var patient = Patient.PatientBuilder().build();
        Assertions.assertThat(patient.applyValidations())
                .isEqualTo(List.of("Civility should be filled",
                        "First name should be filled",
                        "Last name should be filled",
                        "Email should be filled",
                        "Password should be filled",
                        "Phone number should be filled"));
    }
}
