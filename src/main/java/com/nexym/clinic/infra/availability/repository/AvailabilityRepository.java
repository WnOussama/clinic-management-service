package com.nexym.clinic.infra.availability.repository;

import com.nexym.clinic.domain.availability.model.Availability;
import com.nexym.clinic.domain.availability.port.AvailabilityPersistence;
import com.nexym.clinic.infra.availability.dao.AvailabilityDao;
import com.nexym.clinic.infra.availability.mapper.AvailabilityEntityMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AvailabilityRepository implements AvailabilityPersistence {

    private final AvailabilityEntityMapper availabilityEntityMapper;
    private final AvailabilityDao availabilityDao;

    @Override
    public boolean existsById(Long id) {
        return false;
    }

    @Override
    public Availability save(Availability availability) {
        return availabilityEntityMapper.mapToModel(availabilityDao.save(availabilityEntityMapper.mapToEntity(availability)));
    }
}
