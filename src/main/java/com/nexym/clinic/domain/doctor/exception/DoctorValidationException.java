package com.nexym.clinic.domain.doctor.exception;

import com.nexym.clinic.utils.exception.FunctionalException;

import java.util.List;

public class DoctorValidationException extends FunctionalException {

    public DoctorValidationException(String message) {
        super(message);
    }

    public DoctorValidationException(String message, List<String> subErrors) {
        super(message, subErrors);
    }
}
