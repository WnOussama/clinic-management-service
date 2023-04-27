package com.nexym.clinic.domain.speciality;

import com.nexym.clinic.domain.speciality.model.Speciality;
import com.nexym.clinic.domain.speciality.port.SpecialityPersistence;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SpecialityServiceImpl implements SpecialityService{

    @Autowired
    private SpecialityPersistence specialityPersistence;

    @Override
    public List<Speciality> getSpecialitiesList() {
        return specialityPersistence.getSpecialities();
    }
}
