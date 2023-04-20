package com.nexym.clinic.domain.doctor.port;

import com.nexym.clinic.domain.doctor.model.Doctor;
import com.nexym.clinic.domain.doctor.model.DoctorList;

public interface DoctorPersistence {

    Long registerDoctor(Doctor doctor);

    boolean existsByUserEmail(String email);

//    Optional<Doctor> getDoctorById(Long doctorId);

    DoctorList getDoctorList(Integer page, Integer size);

}
