package com.nexym.clinic.domain.availability.port;

import com.nexym.clinic.domain.availability.model.Availability;

public interface AvailabilityPersistence {

    boolean existsById(Long id);

    Availability save(Availability availability);
}
