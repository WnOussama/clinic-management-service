package com.nexym.clinic.config;

import com.nexym.clinic.utils.exception.AccessDeniedException;
import com.nexym.clinic.utils.exception.FunctionalException;
import com.nexym.clinic.utils.exception.TechnicalException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice(basePackages = "com.nexym.clinic")
@Slf4j
public class ClinicManagementExceptionHandler {

    private static ResponseEntity<ClinicApiError> buildApiErrorResponse(String path,
                                                                        HttpStatus status,
                                                                        String message,
                                                                        List<String> subErrors) {
        ClinicApiError apiError = ClinicApiError.builder()
                .path(path)
                .error(status.getReasonPhrase())
                .status(status.value())
                .message(message)
                .subErrors(subErrors)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(apiError, status);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<ClinicApiError> handleAccessDeniedException(AccessDeniedException exception, ServletWebRequest httpServletRequest) {
        return buildApiErrorResponse(httpServletRequest.getRequest().getRequestURI(),
                FORBIDDEN,
                exception.getMessage(),
                Collections.emptyList());
    }

    @ExceptionHandler(value = InternalAuthenticationServiceException.class)
    public ResponseEntity<ClinicApiError> handleInternalAuthenticationServiceException(InternalAuthenticationServiceException exception, ServletWebRequest httpServletRequest) {
        return buildApiErrorResponse(httpServletRequest.getRequest().getRequestURI(),
                FORBIDDEN,
                exception.getMessage(),
                Collections.emptyList());
    }

    @ExceptionHandler(value = FunctionalException.class)
    public ResponseEntity<ClinicApiError> handleFunctionalException(FunctionalException exception, ServletWebRequest httpServletRequest) {
        return buildApiErrorResponse(httpServletRequest.getRequest().getRequestURI(),
                BAD_REQUEST,
                exception.getMessage(),
                exception.getSubErrors());
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<ClinicApiError> handleIllegalArgumentException(IllegalArgumentException exception, ServletWebRequest httpServletRequest) {
        return buildApiErrorResponse(httpServletRequest.getRequest().getRequestURI(),
                BAD_REQUEST,
                exception.getMessage(),
                Collections.emptyList());
    }

    @ExceptionHandler(value = TechnicalException.class)
    public ResponseEntity<ClinicApiError> handleTechnicalException(TechnicalException exception, ServletWebRequest httpServletRequest) {
        log.error(String.format("Intercepted exception: %s", exception.getMessage()), exception);
        var uuid = UUID.randomUUID();
        return buildApiErrorResponse(httpServletRequest.getRequest().getRequestURI(),
                INTERNAL_SERVER_ERROR,
                String.format("Technical error in Clinic Management Service #%s", uuid),
                Collections.emptyList());
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ClinicApiError> handleUnknownException(Exception exception, ServletWebRequest httpServletRequest) {
        log.error(String.format("Intercepted exception: %s", exception.getMessage()), exception);
        var uuid = UUID.randomUUID();
        return buildApiErrorResponse(httpServletRequest.getRequest().getRequestURI(),
                INTERNAL_SERVER_ERROR,
                String.format("Unknown error in Clinic Management Service #%s", uuid),
                Collections.emptyList());
    }


}
