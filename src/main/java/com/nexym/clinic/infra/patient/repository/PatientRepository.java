package com.nexym.clinic.infra.patient.repository;

import com.nexym.clinic.domain.patient.exception.PatientNotFoundException;
import com.nexym.clinic.domain.patient.model.Patient;
import com.nexym.clinic.domain.patient.port.PatientPersistence;
import com.nexym.clinic.infra.patient.dao.PatientDao;
import com.nexym.clinic.infra.patient.mapper.PatientEntityMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class PatientRepository implements PatientPersistence {

    private final PatientDao patientDao;
    private final PatientEntityMapper patientEntityMapper;

    @Override
    public Long save(Patient patient) {
        var savedPatient = patientDao.save(patientEntityMapper.mapToEntity(patient));
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

    @Override
    public void updatePatient(Long id, Patient updateRequest) {
        var existingPatient = patientDao.findById(id)
                .orElseThrow(() -> new PatientNotFoundException(String.format("Patient with id '%s' does not exist", id)));
        patientEntityMapper.update(existingPatient, updateRequest);
        patientDao.save(existingPatient);
    }
}
