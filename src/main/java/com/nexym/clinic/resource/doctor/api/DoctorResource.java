package com.nexym.clinic.resource.doctor.api;

import com.nexym.clinic.api.DoctorsApi;
import com.nexym.clinic.api.model.Doctor;
import com.nexym.clinic.api.model.DoctorListResponse;
import com.nexym.clinic.api.model.DoctorRequest;
import com.nexym.clinic.domain.doctor.DoctorService;
import com.nexym.clinic.resource.doctor.mapper.DoctorWsMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@Slf4j
@AllArgsConstructor
public class DoctorResource implements DoctorsApi {

    private final DoctorWsMapper doctorWsMapper;

    private final DoctorService doctorService;

    @Override
    public ResponseEntity<Void> registerDoctor(DoctorRequest doctorRequest) {
        var savedDoctorId = doctorService.registerDoctor(doctorWsMapper.mapToDoctorModel(doctorRequest));
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedDoctorId).toUri()).build();
    }

    @Override
    public ResponseEntity<DoctorListResponse> searchDoctors(Integer page, Integer size) {
        return ResponseEntity.ok(doctorWsMapper.mapToDoctorResponseList(doctorService.getDoctorList(page, size)));
    }

    @Override
    public ResponseEntity<Void> deleteDoctorById(Long doctorId) {
        doctorService.deleteDoctorById(doctorId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Doctor> getDoctorById(Long doctorId) {
        return ResponseEntity.ok(doctorWsMapper.map(doctorService.getDoctorById(doctorId)));
    }

    @Override
    public ResponseEntity<Doctor> updateDoctorById(Long doctorId, DoctorRequest doctorRequest) {
        doctorService.updateDoctorById(doctorId, doctorWsMapper.mapToDoctorModel(doctorRequest));
        return ResponseEntity.noContent().build();
    }
}
