package com.nexym.clinic.domain.bill.exception;

import com.nexym.clinic.utils.exception.FunctionalException;

import java.util.List;

public class BillValidationException extends FunctionalException {

    public BillValidationException(String message) {
        super(message);
    }

    public BillValidationException(String message, List<String> subErrors) {
        super(message, subErrors);
    }
}
