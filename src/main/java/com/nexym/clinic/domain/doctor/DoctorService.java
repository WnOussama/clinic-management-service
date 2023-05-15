package com.nexym.clinic.domain.doctor;

import com.nexym.clinic.domain.doctor.model.AvailableWithinNext;
import com.nexym.clinic.domain.doctor.model.Doctor;
import com.nexym.clinic.domain.doctor.model.DoctorList;

public interface DoctorService {

    Long registerDoctor(Doctor doctor);

    DoctorList getDoctorList(AvailableWithinNext availableWithinNext, Long specialityId, Integer page, Integer size);

    void deleteDoctorById(Long doctorId);

    Doctor getDoctorById(Long doctorId);

    void updateDoctorById(Long doctorId, Doctor updateRequest);
}
