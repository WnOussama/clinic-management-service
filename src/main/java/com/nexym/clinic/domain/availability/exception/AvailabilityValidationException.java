package com.nexym.clinic.domain.availability.exception;

import com.nexym.clinic.utils.exception.FunctionalException;

import java.util.List;

public class AvailabilityValidationException extends FunctionalException {

    public AvailabilityValidationException(String message) {
        super(message);
    }

    public AvailabilityValidationException(String message, List<String> subErrors) {
        super(message, subErrors);
    }
}
