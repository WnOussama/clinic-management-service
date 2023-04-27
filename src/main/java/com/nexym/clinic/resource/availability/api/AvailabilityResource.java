package com.nexym.clinic.resource.availability.api;

import com.nexym.clinic.api.AvailabilitiesApi;
import com.nexym.clinic.api.model.AvailabilityRequest;
import com.nexym.clinic.domain.availability.AvailabilityService;
import com.nexym.clinic.resource.availability.mapper.AvailabilityWsMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@AllArgsConstructor
public class AvailabilityResource implements AvailabilitiesApi {

    private final AvailabilityWsMapper availabilityWsMapper;

    private final AvailabilityService availabilityService;

    @Override
    public ResponseEntity<Void> addNewAvailability(Long doctorId, AvailabilityRequest availabilityRequest) {
        availabilityService.addNewAvailability(doctorId, availabilityWsMapper.mapToAvailabilityModel(availabilityRequest));
        return ResponseEntity.noContent().build();
    }
}
