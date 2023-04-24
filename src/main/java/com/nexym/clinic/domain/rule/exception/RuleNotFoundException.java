package com.nexym.clinic.domain.rule.exception;

import com.nexym.clinic.utils.exception.FunctionalException;

public class RuleNotFoundException extends FunctionalException {

    public RuleNotFoundException(String message) {
        super(message);
    }
}
