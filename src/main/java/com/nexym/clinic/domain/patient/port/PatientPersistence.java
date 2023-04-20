package com.nexym.clinic.domain.patient.port;

import com.nexym.clinic.domain.patient.model.Patient;

public interface PatientPersistence {

    Long registerPatient(Patient patient);

    boolean existsByUserEmail(String email);

}
