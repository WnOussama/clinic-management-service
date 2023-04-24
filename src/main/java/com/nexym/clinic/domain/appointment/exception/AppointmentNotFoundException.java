package com.nexym.clinic.domain.appointment.exception;

import com.nexym.clinic.utils.exception.FunctionalException;

public class AppointmentNotFoundException extends FunctionalException {

    public AppointmentNotFoundException(String message) {
        super(message);
    }
}
