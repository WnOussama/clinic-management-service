package com.nexym.clinic.infra.availability.mapper;

import com.nexym.clinic.domain.availability.model.Availability;
import com.nexym.clinic.infra.availability.entity.AvailabilityEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface AvailabilityEntityMapper {

    Availability mapToModel(AvailabilityEntity availabilityEntity);

    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "modifiedDate", ignore = true)
    AvailabilityEntity mapToEntity(Availability availabilityModel);

    List<Availability> mapToModelList(List<AvailabilityEntity> availabilityList);
}
