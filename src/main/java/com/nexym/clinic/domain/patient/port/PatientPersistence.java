package com.nexym.clinic.domain.patient.port;

import com.nexym.clinic.domain.patient.model.Patient;
import com.nexym.clinic.domain.patient.model.PatientList;

import java.util.Optional;

public interface PatientPersistence {

    Long createOrUpdate(Patient patient);

    PatientList getPatientList(Integer page, Integer size);

    boolean existsByUserEmail(String email);

    Optional<Patient> getPatientById(Long patientId);

    void deleteById(Long patientId);
}
