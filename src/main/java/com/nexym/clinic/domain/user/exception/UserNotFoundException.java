package com.nexym.clinic.domain.user.exception;

import com.nexym.clinic.utils.exception.FunctionalException;

public class UserNotFoundException extends FunctionalException {

    public UserNotFoundException(String message) {
        super(message);
    }
}
