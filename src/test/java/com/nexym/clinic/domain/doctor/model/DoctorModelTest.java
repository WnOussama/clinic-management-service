package com.nexym.clinic.domain.doctor.model;

import com.nexym.clinic.domain.user.model.Civility;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class DoctorModelTest {

    @Test
    void should_doctor_validate_missing_first_name_fail() {
        var doctor = Doctor.DoctorBuilder()
                .phoneNumber("01122334455")
                .lastName("Doe")
                .civility(Civility.MRS)
                .address("23 Rue des Petits Champs, 75001 Paris, France")
                .specialityId(1L)
                .iban("FR7630001007941234567890185")
                .email("jessie.doe@mail.com")
                .password("Toto2024")
                .build();
        Assertions.assertThat(doctor.applyValidations())
                .isEqualTo(List.of("First name should be filled"));
    }

    @Test
    void should_doctor_validate_missing_last_name_fail() {
        var doctor = Doctor.DoctorBuilder()
                .phoneNumber("01122334455")
                .firstName("Jessie")
                .civility(Civility.MRS)
                .address("23 Rue des Petits Champs, 75001 Paris, France")
                .specialityId(1L)
                .iban("FR7630001007941234567890185")
                .email("jessie.doe@mail.com")
                .password("Toto2024")
                .build();
        Assertions.assertThat(doctor.applyValidations())
                .isEqualTo(List.of("Last name should be filled"));
    }

    @Test
    void should_doctor_validate_missing_all_required_attributes_fail() {
        var doctor = Doctor.DoctorBuilder().build();
        Assertions.assertThat(doctor.applyValidations())
                .isEqualTo(List.of("Civility should be filled",
                        "First name should be filled",
                        "Last name should be filled",
                        "Email should be filled",
                        "Password should be filled",
                        "Phone number should be filled",
                        "Address should be filled",
                        "Iban should be filled",
                        "Speciality should be filled"
                        ));
    }
}
