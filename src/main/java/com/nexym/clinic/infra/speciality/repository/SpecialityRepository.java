package com.nexym.clinic.infra.speciality.repository;

import com.nexym.clinic.domain.speciality.model.Speciality;
import com.nexym.clinic.domain.speciality.port.SpecialityPersistence;
import com.nexym.clinic.infra.speciality.dao.SpecialityDao;
import com.nexym.clinic.infra.speciality.mapper.SpecialityEntityMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SpecialityRepository implements SpecialityPersistence {

    private final SpecialityDao specialityDao;
    private final SpecialityEntityMapper specialityEntityMapper;

    @Override
    public List<Speciality> getSpecialities() {
        var specialityEntityList = specialityDao.findAll();
        return specialityEntityMapper.mapToModelList(specialityEntityList);
    }

    @Override
    public Optional<Speciality> getSpecialityById(Long specialityId) {
        return specialityDao.findById(specialityId).map(specialityEntityMapper::map);
    }
}
