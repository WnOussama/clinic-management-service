package com.nexym.clinic.utils.exception;

import java.util.List;

public class FunctionalException extends RuntimeException {

    private final List<String> subErrors;

    public FunctionalException(String message) {
        super(message);
        this.subErrors = null;
    }

    public FunctionalException(String message, List<String> subErrors) {
        super(message);
        this.subErrors = subErrors;
    }

    public List<String> getSubErrors() {
        return subErrors;
    }
}
