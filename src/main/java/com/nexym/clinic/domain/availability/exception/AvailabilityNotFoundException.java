package com.nexym.clinic.domain.availability.exception;

import com.nexym.clinic.utils.exception.FunctionalException;

public class AvailabilityNotFoundException extends FunctionalException {

    public AvailabilityNotFoundException(String message) {
        super(message);
    }
}
