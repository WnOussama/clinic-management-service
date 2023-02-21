package com.nexym.clinic.domain.user;

import com.nexym.clinic.domain.user.exception.UserNotFoundException;
import com.nexym.clinic.domain.user.model.Civility;
import com.nexym.clinic.domain.user.model.User;
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
@Sql("/user/db/user-test-data.sql")
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    public void should_find_user_by_id_success() {
        // Given
        var userId = 1L;
        var now = LocalDateTime.now();
        // When
        var foundUser = userService.getUserById(userId);
        // Then
        Assertions.assertThat(foundUser).isEqualTo(getUser(now));
    }

    @Test
    public void should_register_user_success() {
        // Given
        var user = User.builder()
                .civility(Civility.MR)
                .firstName("Ali")
                .lastName("Baba")
                .email("ali.baba@mail.com")
                .phoneNumber("0223344311")
                .password("Toto2022")
                .build();

        // When
        var foundUser = userService.registerUser(user);
        // Then
        Assertions.assertThat(foundUser).isEqualTo(1L);
    }

    @Test
    public void should_find_user_by_id_not_found_fail() {
        // Given
        var userId = 523L;
        // When
        ThrowableAssert.ThrowingCallable callable = () -> userService.getUserById(userId);
        // Then
        Assertions.assertThatThrownBy(callable)
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User with id '523' does not exist");
    }

    private static User getUser(LocalDateTime now) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return User.builder()
                .id(1L)
                .civility(Civility.MR)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@mail.com")
                .phoneNumber("01122334455")
                .creationDate(LocalDateTime.parse("2023-02-21 10:50:54", formatter))
                .build();
    }

}
