package com.nexym.clinic.domain.speciality.exception;

import com.nexym.clinic.utils.exception.FunctionalException;

public class SpecialityNotFoundException extends FunctionalException {

    public SpecialityNotFoundException(String message) {
        super(message);
    }
}
