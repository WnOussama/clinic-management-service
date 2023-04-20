package com.nexym.clinic.domain.availability;

import com.nexym.clinic.domain.availability.model.Availability;

public interface AvailabilityService {

    Availability addAvailabilityByDoctorId(Long doctorId, Availability availability);
}
