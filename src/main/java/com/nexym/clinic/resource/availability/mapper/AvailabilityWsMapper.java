package com.nexym.clinic.resource.availability.mapper;

import com.nexym.clinic.api.model.Availability;
import com.nexym.clinic.api.model.AvailabilityRequest;
import org.mapstruct.*;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface AvailabilityWsMapper {


    Availability mapToApiModel(com.nexym.clinic.domain.availability.model.Availability availabilityModel);

    default LocalDateTime mapTime(OffsetDateTime date) {
        if (date != null) {
            return date.toLocalDateTime();
        }
        return null;
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "modifiedDate", ignore = true)
    com.nexym.clinic.domain.availability.model.Availability mapToAvailabilityModel(AvailabilityRequest availabilityRequest);

    default OffsetDateTime map(LocalDateTime date) {
        if (date != null) {
            return date.atOffset(ZoneOffset.UTC);
        }
        return null;
    }
}
