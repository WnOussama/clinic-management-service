package com.nexym.clinic.infra.patient.repository;

import com.nexym.clinic.domain.patient.exception.PatientNotFoundException;
import com.nexym.clinic.domain.patient.model.Patient;
import com.nexym.clinic.domain.patient.port.PatientPersistence;
import com.nexym.clinic.infra.patient.dao.PatientDao;
import com.nexym.clinic.infra.patient.entity.PatientEntity;
import com.nexym.clinic.infra.patient.mapper.PatientEntityMapper;
import com.nexym.clinic.utils.FormatUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class PatientRepository implements PatientPersistence {

    private final PatientDao patientDao;
    private final PatientEntityMapper patientEntityMapper;

    @Override
    public Long createOrUpdate(Patient patient) {
        PatientEntity savedPatient;
        if (patient.getId() != null) {
            var existingPatient = patientDao.findById(patient.getId())
                    .orElseThrow(() -> new PatientNotFoundException(String.format("Patient with id '%s' does not exist", patient.getId())));
            if (FormatUtil.isFilled(existingPatient.getAppointments())) {
                existingPatient.getAppointments().clear();
            }
            patientEntityMapper.update(existingPatient, patient);
            savedPatient = patientDao.save(existingPatient);
        } else {
            // new entity instance
            savedPatient = patientDao.save(patientEntityMapper.mapToEntity(patient));
        }
        return savedPatient.getId();
    }

    @Override
    public boolean existsByUserEmail(String email) {
        return patientDao.existsByUserEmail(email);
    }

    @Override
    public Optional<Patient> getPatientById(Long patientId) {
        return patientDao.findById(patientId).map(patientEntityMapper::mapToModel);
    }

    @Override
    public void deleteById(Long patientId) {
        patientDao.deleteById(patientId);
    }
}
