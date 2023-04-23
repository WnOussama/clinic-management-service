package com.nexym.clinic.domain.availability;

import com.nexym.clinic.domain.availability.model.Availability;

public interface AvailabilityService {

    void addNewAvailability(Long doctorId, Availability availability);
}
