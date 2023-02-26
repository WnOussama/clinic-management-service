package com.nexym.clinic.domain.user;

import com.nexym.clinic.domain.user.exception.UserNotFoundException;
import com.nexym.clinic.domain.user.exception.UserValidationException;
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
@Sql("/user/db/data-27-02-2023.sql")
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    void should_find_user_by_id_success() {
        // Given
        var userId = 1L;
        // When
        var foundUser = userService.getUserById(userId);
        // Then
        Assertions.assertThat(foundUser).isEqualTo(getUser(Civility.MR,
                "John",
                "Doe",
                "01122334455",
                "john.doe@mail.com",
                "$2a$10$PRlKa/dbKFsBT4IuIbCPKOvOx7GZDjLDi0uLCe9Mgc13QO8OkF37W"));
    }

    @Test
    void should_register_user_success() {
        // Given
        var user = getUser(Civility.MRS,
                "Ali",
                "Baba",
                "0223344311",
                "ali.baba@mail.com",
                "Toto2022");

        // When
        var foundUser = userService.registerUser(user);
        // Then
        Assertions.assertThat(foundUser).isEqualTo(1L);
    }

    @Test
    void should_register_user_existing_same_email_fail() {
        // Given
        var user = getUser(Civility.MRS,
                "Ali",
                "Baba",
                "0223344311",
                "john.doe@mail.com",
                "Toto2022");

        // When
        ThrowableAssert.ThrowingCallable callable = () -> userService.registerUser(user);
        // Then
        Assertions.assertThatThrownBy(callable)
                .isInstanceOf(UserValidationException.class)
                .hasMessage("User with email 'john.doe@mail.com' already exists");
    }

    @Test
    void should_register_user_missing_required_attribute_fail() {
        // When
        ThrowableAssert.ThrowingCallable callable = () -> userService.registerUser(User.builder().build());
        // Then
        Assertions.assertThatThrownBy(callable)
                .isInstanceOf(UserValidationException.class)
                .hasMessage("Failed to validate user request");
    }

    @Test
    void should_find_user_by_id_not_found_fail() {
        // Given
        var userId = 523L;
        // When
        ThrowableAssert.ThrowingCallable callable = () -> userService.getUserById(userId);
        // Then
        Assertions.assertThatThrownBy(callable)
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User with id '523' does not exist");
    }

    @Test
    void should_load_user_by_username_not_found_fail() {
        ThrowableAssert.ThrowingCallable callable = () -> userService.loadUserByUsername("not_found@mail.com");
        // Then
        Assertions.assertThatThrownBy(callable)
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User with email 'not_found@mail.com' not found");
    }

    @Test
    void should_load_user_by_username_success() {
        String username = "john.doe@mail.com";
        var details = userService.loadUserByUsername(username);
        // Then
        Assertions.assertThat(details).isNotNull();
        Assertions.assertThat(details.getUsername()).isEqualTo(username);
    }

    private static User getUser(Civility civility, String firstName, String lastName, String phoneNumber, String email, String password) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return User.builder()
                .id(1L)
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
