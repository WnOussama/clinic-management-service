package com.nexym.clinic.domain.patient;

import com.nexym.clinic.domain.patient.exception.PatientNotFoundException;
import com.nexym.clinic.domain.patient.exception.PatientValidationException;
import com.nexym.clinic.domain.patient.model.Patient;
import com.nexym.clinic.domain.patient.model.PatientList;
import com.nexym.clinic.domain.patient.port.PatientPersistence;
import com.nexym.clinic.domain.user.exception.UserValidationException;
import com.nexym.clinic.utils.FormatUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PatientServiceImpl implements PatientService {

    @Autowired
    private PatientPersistence patientPersistence;

    @Override
    public Long registerPatient(Patient patient) {
        var errorList = patient.applyValidations();
        if (FormatUtil.isFilled(errorList)) {
            throw new PatientValidationException("Failed to validate patient request", errorList);
        } else {
            var patientEmail = patient.getEmail();
            if (patientPersistence.existsByUserEmail(patientEmail)) {
                throw new UserValidationException(String.format("User with email '%s' already exists", patientEmail));
            }
            return patientPersistence.createOrUpdate(patient);
        }
    }

    @Override
    public PatientList getPatientList(Integer page, Integer size) {
        return patientPersistence.getPatientList(page, size);
    }

    @Override
    public Patient getPatientById(Long patientId) {
        return patientPersistence.getPatientById(patientId)
                .orElseThrow(() -> new PatientNotFoundException(String.format("Patient with id '%s' does not exist", patientId)));
    }

    @Override
    public void deletePatientById(Long patientId) {
        getPatientById(patientId);
        patientPersistence.deleteById(patientId);
    }

    @Override
    public void updatePatientById(Long patientId, Patient request) {
        request.setId(patientId);
        patientPersistence.createOrUpdate(request);
    }
}
