package com.nexym.clinic.domain.patient;

import com.nexym.clinic.domain.patient.model.Patient;
import com.nexym.clinic.domain.patient.model.PatientList;

public interface PatientService {

    Long registerPatient(Patient patient);

    PatientList getPatientList(Integer page, Integer size);

    Patient getPatientById(Long patientId);

    void deletePatientById(Long patientId);

    void updatePatientById(Long patientId, Patient patient);
}
