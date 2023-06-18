package com.nexym.clinic.infra.patient.repository;

import com.nexym.clinic.domain.patient.exception.PatientNotFoundException;
import com.nexym.clinic.domain.patient.model.Patient;
import com.nexym.clinic.domain.patient.model.PatientList;
import com.nexym.clinic.domain.patient.port.PatientPersistence;
import com.nexym.clinic.infra.patient.dao.PatientDao;
import com.nexym.clinic.infra.patient.mapper.PatientEntityMapper;
import com.nexym.clinic.infra.user.dao.UserDao;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class PatientRepository implements PatientPersistence {

    private final PatientDao patientDao;
    private final UserDao userDao;
    private final PatientEntityMapper patientEntityMapper;

    @Override
    public Long addNewPatient(Patient patient) {
        var savedPatient = patientDao.save(patientEntityMapper.mapToEntity(patient));
        return savedPatient.getId();
    }

    @Override
    public void updatePatientDetails(Patient patient) {
        var existingPatient = patientDao.findById(patient.getId())
                .orElseThrow(() -> new PatientNotFoundException(String.format("Patient with id '%s' does not exist", patient.getId())));
        patientEntityMapper.update(existingPatient, patient);
        patientDao.save(existingPatient);
    }

    @Override
    public PatientList getPatientList(Integer page, Integer size) {
        var pageable = PageRequest.of(page, size);
        var doctorEntityList = patientDao.findAll(pageable);
        return patientEntityMapper.mapToModelList(doctorEntityList);
    }

    @Override
    public boolean existsByUserEmail(String email) {
        return userDao.existsByEmail(email);
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
    public Optional<Patient> getPatientByEmail(String email) {
        return patientDao.findByUserEmail(email).map(patientEntityMapper::mapToModel);
    }
}
