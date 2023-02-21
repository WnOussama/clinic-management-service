package com.nexym.clinic.domain.user.exception;

import com.nexym.clinic.utils.exception.FunctionalException;

import java.util.List;

public class UserValidationException extends FunctionalException {

    public UserValidationException(String message) {
        super(message);
    }

    public UserValidationException(String message, List<String> subErrors) {
        super(message, subErrors);
    }
}
