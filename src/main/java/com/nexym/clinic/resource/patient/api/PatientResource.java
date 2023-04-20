package com.nexym.clinic.resource.patient.api;

import com.nexym.clinic.api.PatientsApi;
import com.nexym.clinic.api.model.PatientRequest;
import com.nexym.clinic.domain.patient.PatientService;
import com.nexym.clinic.resource.patient.mapper.PatientWsMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@Slf4j
@AllArgsConstructor
public class PatientResource implements PatientsApi {

    private final PatientWsMapper patientWsMapper;

    private final PatientService patientService;

    @Override
    public ResponseEntity<Void> registerPatient(PatientRequest patientRequest) {
        var savedPatientId = patientService.registerPatient(patientWsMapper.mapToPatientModel(patientRequest));
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedPatientId).toUri()).build();
    }
}
