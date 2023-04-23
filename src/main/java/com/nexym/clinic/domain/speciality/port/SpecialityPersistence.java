package com.nexym.clinic.domain.speciality.port;

import com.nexym.clinic.domain.speciality.model.Speciality;

import java.util.List;
import java.util.Optional;

public interface SpecialityPersistence {

    List<Speciality> getSpecialities();

    Optional<Speciality> getSpecialityById(Long specialityId);
}
