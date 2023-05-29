package com.nexym.clinic.domain.speciality;

import com.nexym.clinic.domain.speciality.model.Speciality;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
class SpecialityServiceTest {

    @Autowired
    private SpecialityService specialityService;

    @Test
    void should_get_speciality_list_success() {
        var specialityList = specialityService.getSpecialitiesList();
        // Then
        Assertions.assertThat(specialityList).isEqualTo(List.of(Speciality.builder()
                        .id(1L)
                        .name("Médecin généraliste")
                        .appointmentDuration(15L)
                        .appointmentFee(50L)
                        .build(),
                Speciality.builder()
                        .id(51L)
                        .name("Orthopédiste")
                        .appointmentDuration(30L)
                        .appointmentFee(60L)
                        .build(),
                Speciality.builder()
                        .id(101L)
                        .name("Ophtalmologue")
                        .appointmentDuration(30L)
                        .appointmentFee(60L)
                        .build(),
                Speciality.builder()
                        .id(151L)
                        .name("Pédiatre")
                        .appointmentDuration(45L)
                        .appointmentFee(80L)
                        .build(),
                Speciality.builder()
                        .id(201L)
                        .name("Dentiste")
                        .appointmentDuration(60L)
                        .appointmentFee(100L)
                        .build()));
    }
}
