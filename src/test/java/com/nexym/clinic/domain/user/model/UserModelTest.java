package com.nexym.clinic.domain.user.model;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class UserModelTest {

    @Test
    void should_user_validate_missing_first_name_fail() {
        var user = User.builder()
                .phoneNumber("01134345")
                .lastName("Doe")
                .civility(Civility.MR)
                .email("john.doe@mail.com")
                .password("Toto2024")
                .build();
        Assertions.assertThat(user.applyValidations())
                .isEqualTo(List.of("First name should be filled"));
    }

    @Test
    void should_user_validate_missing_last_name_fail() {
        var user = User.builder()
                .phoneNumber("01134345")
                .firstName("John")
                .civility(Civility.MR)
                .email("john.doe@mail.com")
                .password("Toto2024")
                .build();
        Assertions.assertThat(user.applyValidations())
                .isEqualTo(List.of("Last name should be filled"));
    }

    @Test
    void should_user_validate_missing_all_required_attributes_fail() {
        var user = User.builder().build();
        Assertions.assertThat(user.applyValidations())
                .isEqualTo(List.of("Civility should be filled",
                        "First name should be filled",
                        "Last name should be filled",
                        "Email should be filled",
                        "Password should be filled",
                        "Phone number should be filled"));
    }
}
