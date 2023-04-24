package com.nexym.clinic.domain.patient.exception;

import com.nexym.clinic.utils.exception.FunctionalException;

public class PatientNotFoundException extends FunctionalException {

    public PatientNotFoundException(String message) {
        super(message);
    }
}
