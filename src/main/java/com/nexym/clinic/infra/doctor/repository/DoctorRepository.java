package com.nexym.clinic.infra.doctor.repository;

import com.nexym.clinic.domain.doctor.exception.DoctorNotFoundException;
import com.nexym.clinic.domain.doctor.model.Doctor;
import com.nexym.clinic.domain.doctor.model.DoctorList;
import com.nexym.clinic.domain.doctor.port.DoctorPersistence;
import com.nexym.clinic.infra.doctor.dao.DoctorDao;
import com.nexym.clinic.infra.doctor.entity.DoctorEntity;
import com.nexym.clinic.infra.doctor.mapper.DoctorEntityMapper;
import com.nexym.clinic.infra.user.dao.UserDao;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class DoctorRepository implements DoctorPersistence {

    private final DoctorDao doctorDao;
    private final UserDao userDao;
    private final DoctorEntityMapper doctorEntityMapper;


    @Override
    public Long createOrUpdate(Doctor doctor) {
        DoctorEntity doctorEntity;
        if (doctor.getId() != null) { // update case
            var existingDoctor = doctorDao.findById(doctor.getId())
                    .orElseThrow(() -> new DoctorNotFoundException(String.format("Doctor with id '%s' does not exist", doctor.getId())));
            doctorEntityMapper.update(existingDoctor, doctor);
            doctorEntity = doctorDao.save(existingDoctor);
        } else {
            // new doctor instance
            doctorEntity = doctorDao.save(doctorEntityMapper.mapToEntity(doctor));
        }
        return doctorEntity.getId();
    }

    @Override
    public boolean existsByUserEmail(String email) {
        return userDao.existsByEmail(email);
    }

    @Override
    public DoctorList getDoctorList(Integer page, Integer size) {
        var pageable = PageRequest.of(page, size);
        var doctorEntityList = doctorDao.findAll(pageable);
        return doctorEntityMapper.mapToModelList(doctorEntityList);
    }

    @Override
    public Optional<Doctor> getDoctorById(Long doctorId) {
        return doctorDao.findById(doctorId).map(doctorEntityMapper::mapToModel);
    }

    @Override
    public void deleteDoctorById(Long doctorId) {
        doctorDao.deleteById(doctorId);
    }
}
