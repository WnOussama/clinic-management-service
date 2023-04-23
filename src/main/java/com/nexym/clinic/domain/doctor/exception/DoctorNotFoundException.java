package com.nexym.clinic.domain.doctor.exception;

import com.nexym.clinic.utils.exception.FunctionalException;

public class DoctorNotFoundException extends FunctionalException {

    public DoctorNotFoundException(String message) {
        super(message);
    }
}
