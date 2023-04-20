package com.nexym.clinic.infra.patient.repository;

import com.nexym.clinic.domain.patient.model.Patient;
import com.nexym.clinic.domain.patient.port.PatientPersistence;
import com.nexym.clinic.infra.patient.dao.PatientDao;
import com.nexym.clinic.infra.patient.mapper.PatientEntityMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PatientRepository implements PatientPersistence {

    private final PatientDao patientDao;
    private final PatientEntityMapper patientEntityMapper;

    @Override
    public Long registerPatient(Patient patient) {
        var savedPatient = patientDao.save(patientEntityMapper.mapToEntity(patient));
        return savedPatient.getId();
    }

    @Override
    public boolean existsByUserEmail(String email) {
        return patientDao.existsByUserEmail(email);
    }
}
