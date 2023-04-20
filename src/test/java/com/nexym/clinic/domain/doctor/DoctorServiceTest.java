package com.nexym.clinic.domain.doctor;

import com.nexym.clinic.domain.doctor.exception.DoctorValidationException;
import com.nexym.clinic.domain.doctor.model.Doctor;
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
class DoctorServiceTest {

    @Autowired
    private DoctorService doctorService;

    @Test
    void should_register_user_success() {
        // Given
        var doctor = getDoctor(Civility.MRS,
                "Ali",
                "Baba",
                "0223344311",
                "ali.baba@mail.com",
                "Toto2022",
                "23 Rue des Petits Champs, 75001 Paris, France",
                1L);

        // When
        var foundUser = doctorService.registerDoctor(doctor);
        // Then
        Assertions.assertThat(foundUser).isEqualTo(1L);
    }

    @Test
    void should_register_doctor_existing_same_email_fail() {
        // Given
        var doctor = getDoctor(Civility.MRS,
                "Ali",
                "Baba",
                "0223344311",
                "john.doe@mail.com",
                "Toto2022",
                "23 Rue des Petits Champs, 75001 Paris, France",
                1L);

        // When
        ThrowableAssert.ThrowingCallable callable = () -> doctorService.registerDoctor(doctor);
        // Then
        Assertions.assertThatThrownBy(callable)
                .isInstanceOf(UserValidationException.class)
                .hasMessage("User with email 'john.doe@mail.com' already exists");
    }

    @Test
    void should_register_doctor_missing_required_attribute_fail() {
        // When
        ThrowableAssert.ThrowingCallable callable = () -> doctorService.registerDoctor(Doctor.builder().build());
        // Then
        Assertions.assertThatThrownBy(callable)
                .isInstanceOf(DoctorValidationException.class)
                .hasMessage("Failed to validate doctor request");
    }

    @Test
    void should_search_doctors_success() {
        var doctorList = doctorService.getDoctorList(0, 10);
        // Then
        Assertions.assertThat(doctorList).isEqualTo(List.of(getDoctor(Civility.MRS,
                "Jessie",
                "Doe",
                "01122334455",
                "jessie.doe@mail.com",
                "$2a$10$PRlKa/dbKFsBT4IuIbCPKOvOx7GZDjLDi0uLCe9Mgc13QO8OkF37W",
                "23 Rue des Petits Champs, 75001 Paris, France",
                1L)));
    }

    private static Doctor getDoctor(Civility civility, String firstName, String lastName, String phoneNumber, String email, String password, String address, Long specialityId) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return Doctor.builder()
                .id(1L)
                .userId(2L)
                .civility(civility)
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .phoneNumber(phoneNumber)
                .password(password)
                .address(address)
                .specialityId(specialityId)
                .creationDate(LocalDateTime.parse("2023-04-06 03:16:54", formatter))
                .build();
    }
}
