package com.nexym.clinic.domain.availability;

import com.nexym.clinic.domain.availability.model.Availability;
import com.nexym.clinic.domain.availability.port.AvailabilityPersistence;
import com.nexym.clinic.domain.doctor.port.DoctorPersistence;
import com.nexym.clinic.infra.availability.mapper.AvailabilityEntityMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AvailabilityServiceImpl implements AvailabilityService {

    @Autowired
    private AvailabilityPersistence availabilityPersistence;

    @Autowired
    private DoctorPersistence doctorPersistence;

    @Autowired
    private AvailabilityEntityMapper availabilityEntityMapper;

    @Override
    public Availability addAvailabilityByDoctorId(Long doctorId, Availability availability) {
//        var doctor = doctorPersistence.getDoctorById(doctorId)
//                .orElseThrow(() -> new DoctorNotFoundException(String.format("Doctor with id '%d' not found", doctorId)));
//        var availabilityId = availability.getId();
//        if(availabilityPersistence.existsById(availabilityId)){
//            throw new AvailabilityNotFoundException(String.format("Availability with id '%s' already exists", availabilityId));
//        }
//        availability.setDoctorId(doctorId);
//        return availabilityPersistence.save(availability);
        return null;
    }
}
