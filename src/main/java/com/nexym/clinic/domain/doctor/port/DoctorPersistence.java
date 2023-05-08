package com.nexym.clinic.domain.doctor.port;

import com.nexym.clinic.domain.availability.model.Availability;
import com.nexym.clinic.domain.doctor.model.Doctor;
import com.nexym.clinic.domain.doctor.model.DoctorList;

import java.util.Optional;

public interface DoctorPersistence {

    Long addNewDoctor(Doctor doctor);

    void updateDoctorDetails(Doctor doctor);

    void addNewAvailability(Long doctorId, Availability availability);

    boolean existsByUserEmail(String email);

    DoctorList getDoctorList(Integer page, Integer size);

    Optional<Doctor> getDoctorById(Long doctorId);

    void deleteDoctorById(Long doctorId);

    Optional<Doctor> getDoctorByEmail(String email);
}
