package com.nexym.clinic.domain.appointment.exception;

import com.nexym.clinic.utils.exception.FunctionalException;

public class AppointmentValidationException extends FunctionalException {
    public AppointmentValidationException(String message) {
        super(message);
    }
}
