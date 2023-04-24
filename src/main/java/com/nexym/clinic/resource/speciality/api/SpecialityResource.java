package com.nexym.clinic.resource.speciality.api;

import com.nexym.clinic.api.SpecialitiesApi;
import com.nexym.clinic.api.model.Speciality;
import com.nexym.clinic.domain.speciality.SpecialityService;
import com.nexym.clinic.resource.speciality.mapper.SpecialityWsMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
public class SpecialityResource implements SpecialitiesApi {

    private final SpecialityWsMapper specialityWsMapper;

    private final SpecialityService specialityService;

    @Override
    public ResponseEntity<List<Speciality>> getSpecialities() {
        return ResponseEntity.ok(specialityWsMapper.mapToSpecialityResponseList(specialityService.getSpecialities()));
    }

}
