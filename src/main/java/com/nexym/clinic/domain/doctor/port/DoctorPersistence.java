package com.nexym.clinic.domain.doctor.port;

import com.nexym.clinic.domain.doctor.model.Doctor;
import com.nexym.clinic.domain.doctor.model.DoctorList;

import java.util.Optional;

public interface DoctorPersistence {

    Long createOrUpdate(Doctor doctor);

    boolean existsByUserEmail(String email);

    DoctorList getDoctorList(Integer page, Integer size);

    Optional<Doctor> getDoctorById(Long doctorId);

    void deleteDoctorById(Long doctorId);
}
