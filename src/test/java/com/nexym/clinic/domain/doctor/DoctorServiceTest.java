package com.nexym.clinic.domain.doctor;

import com.nexym.clinic.domain.doctor.exception.DoctorNotFoundException;
import com.nexym.clinic.domain.doctor.exception.DoctorValidationException;
import com.nexym.clinic.domain.doctor.model.Doctor;
import com.nexym.clinic.domain.doctor.model.DoctorList;
import com.nexym.clinic.domain.user.exception.UserValidationException;
import com.nexym.clinic.domain.user.model.Civility;
import com.nexym.clinic.resource.doctor.mapper.DoctorWsMapper;
import com.nexym.clinic.utils.exception.FunctionalException;
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

    @Autowired
    private DoctorWsMapper doctorWsMapper;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

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
        ThrowableAssert.ThrowingCallable callable = () -> doctorService.registerDoctor(Doctor.DoctorBuilder().build());
        // Then
        Assertions.assertThatThrownBy(callable)
                .isInstanceOf(DoctorValidationException.class)
                .hasMessage("Failed to validate doctor request");
    }

    @Test
    void should_update_doctor_by_id_speciality_is_not_recognized_fail() {
        // When
        ThrowableAssert.ThrowingCallable callable = () -> doctorService.updateDoctorById(3L, getDoctor(Civility.MR,
                "Marshall",
                "Baba",
                "0223344311",
                "marshall.baba@mail.com",
                "Toto2022",
                "Paris, France",
                4L));
        // Then
        Assertions.assertThatThrownBy(callable)
                .isInstanceOf(FunctionalException.class)
                .hasMessage("Speciality with id '4' is not recognized");
    }

    @Test
    void should_get_doctor_by_id_not_found_fail() {
        // When
        ThrowableAssert.ThrowingCallable callable = () -> doctorService.getDoctorById(3L);
        // Then
        Assertions.assertThatThrownBy(callable)
                .isInstanceOf(DoctorNotFoundException.class)
                .hasMessage("Doctor with id '3' does not exist");
    }

    @Test
    void should_delete_doctor_by_id_not_exist_fail() {
        // When
        ThrowableAssert.ThrowingCallable callable = () -> doctorService.deleteDoctorById(3L);
        // Then
        Assertions.assertThatThrownBy(callable)
                .isInstanceOf(DoctorNotFoundException.class)
                .hasMessage("Doctor with id '3' does not exist");
    }

    @Test
    void should_update_doctor_by_id_not_exist_fail() {
        // When
        ThrowableAssert.ThrowingCallable callable = () -> doctorService.updateDoctorById(3L, getDoctor(Civility.MR,
                "Marshall",
                "Baba",
                "0223344311",
                "marshall.baba@mail.com",
                "Toto2022",
                "Paris, France",
                1L));
        // Then
        Assertions.assertThatThrownBy(callable)
                .isInstanceOf(DoctorNotFoundException.class)
                .hasMessage("Doctor with id '3' does not exist");
    }

    @Test
    void should_update_doctor_success() {
        // Given
        var doctor = getDoctor(Civility.MR,
                "Ali",
                "Baba",
                "0223344311",
                "ali.baba@mail.com",
                "Toto2022",
                "Paris, France",
                1L);

        // When
        doctorService.updateDoctorById(1L, doctor);
        var updatedDoctor = doctorService.getDoctorById(1L);
        // Then
        Assertions.assertThat(updatedDoctor.getId()).isEqualTo(doctor.getId());
        Assertions.assertThat(updatedDoctor.getFirstName()).isEqualTo(doctor.getFirstName());
        Assertions.assertThat(updatedDoctor.getLastName()).isEqualTo(doctor.getLastName());
        Assertions.assertThat(updatedDoctor.getEmail()).isEqualTo(doctor.getEmail());
        Assertions.assertThat(updatedDoctor.getSpecialityId()).isEqualTo(doctor.getSpecialityId());
        Assertions.assertThat(updatedDoctor.getAddress()).isEqualTo(doctor.getAddress());
        Assertions.assertThat(updatedDoctor.getIban()).isEqualTo(doctor.getIban());
    }

    @Test
    void should_register_doctor_success() {
        // Given
        var doctor = getDoctor(Civility.MRS,
                "Ali",
                "Baba",
                "0223344311",
                "ali.baba@mail.com",
                "Toto2022",
                "Paris, France",
                1L);

        // When
        var foundDoctor = doctorService.registerDoctor(doctor);
        // Then
        Assertions.assertThat(foundDoctor).isEqualTo(1L);
    }

    @Test
    void should_search_doctors_success() {
        var doctorList = doctorService.getDoctorList(doctorWsMapper.mapToEnum("TODAY"), 1L, 0, 10);
        // Then
        var items = List.of(Doctor.DoctorBuilder()
                .id(1L)
                .ruleId(1L)
                .specialityId(1L)
                .iban("FR7630001007941234567890185")
                .address("23 Rue des Petits Champs, 75001 Paris, France")
                .creationDate(LocalDateTime.parse("2023-04-06 03:16:54", formatter))
                .availabilities(List.of())
                .build());
        Assertions.assertThat(doctorList).isEqualTo(DoctorList.builder()
                .items(items)
                .first(true)
                .last(true)
                .number(0)
                .totalElements(1L)
                .numberOfElements(1)
                .totalPages(1)
                .build());
    }

    private static Doctor getDoctor(Civility civility, String firstName, String lastName, String phoneNumber, String email, String password, String address, Long specialityId) {
        return Doctor.DoctorBuilder()
                .id(1L)
                .civility(civility)
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .phoneNumber(phoneNumber)
                .password(password)
                .address(address)
                .iban("FR7630001007941234567890185")
                .specialityId(specialityId)
                .creationDate(LocalDateTime.parse("2023-04-06 03:16:54", formatter))
                .build();
    }
}
