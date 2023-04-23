package com.nexym.clinic.domain.patient.exception;

import com.nexym.clinic.utils.exception.FunctionalException;

import java.util.List;

public class PatientValidationException extends FunctionalException {

    public PatientValidationException(String message) {
        super(message);
    }

    public PatientValidationException(String message, List<String> subErrors) {
        super(message, subErrors);
    }
}
