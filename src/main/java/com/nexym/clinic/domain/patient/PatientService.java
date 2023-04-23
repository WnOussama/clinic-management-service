package com.nexym.clinic.domain.patient;

import com.nexym.clinic.domain.patient.model.Patient;

public interface PatientService {

    Long registerPatient(Patient patient);

    Patient getPatientById(Long patientId);

    void deletePatientById(Long patientId);

    void updatePatientById(Long patientId, Patient patient);
}
