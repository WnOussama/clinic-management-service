package com.nexym.clinic.infra.doctor.repository;

import com.nexym.clinic.domain.doctor.model.Doctor;
import com.nexym.clinic.domain.doctor.model.DoctorList;
import com.nexym.clinic.domain.doctor.port.DoctorPersistence;
import com.nexym.clinic.infra.doctor.dao.DoctorDao;
import com.nexym.clinic.infra.doctor.mapper.DoctorEntityMapper;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DoctorRepository implements DoctorPersistence {

    private final DoctorDao doctorDao;
    private final DoctorEntityMapper doctorEntityMapper;


    @Override
    public Long registerDoctor(Doctor doctor) {
        var savedDoctor = doctorDao.save(doctorEntityMapper.mapToEntity(doctor));
        return savedDoctor.getId();
    }

    @Override
    public boolean existsByUserEmail(String email) {
        return doctorDao.existsByUserEmail(email);
    }

    @Override
    public DoctorList getDoctorList(Integer page, Integer size) {
        var pageable = PageRequest.of(page, size);
        var doctorEntityList = doctorDao.findAll(pageable);
        return doctorEntityMapper.mapToModelList(doctorEntityList);
    }

}
