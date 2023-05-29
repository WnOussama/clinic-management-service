package com.nexym.clinic.domain.bill.exception;

import com.nexym.clinic.utils.exception.FunctionalException;

public class BillNotFoundException extends FunctionalException {

    public BillNotFoundException(String message) {
        super(message);
    }
}
