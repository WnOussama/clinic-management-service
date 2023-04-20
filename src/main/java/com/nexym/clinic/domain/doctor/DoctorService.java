package com.nexym.clinic.domain.doctor;

import com.nexym.clinic.domain.doctor.model.Doctor;
import com.nexym.clinic.domain.doctor.model.DoctorList;

public interface DoctorService {

    Long registerDoctor(Doctor doctor);

    DoctorList getDoctorList(Integer page, Integer size);


}
