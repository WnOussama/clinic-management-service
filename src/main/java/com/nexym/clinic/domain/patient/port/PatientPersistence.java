package com.nexym.clinic.domain.patient.port;

import com.nexym.clinic.domain.patient.model.Patient;

import java.util.Optional;

public interface PatientPersistence {

    Long createOrUpdate(Patient patient);

    boolean existsByUserEmail(String email);
    Optional<Patient> getPatientById(Long patientId);

    void deleteById(Long patientId);
}
