package com.nexym.clinic.resource.availability.mapper;

import com.nexym.clinic.api.model.AvailabilityRequest;
import org.mapstruct.*;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface AvailabilityWsMapper {


    default LocalDateTime mapTime(OffsetDateTime date) {
        if (date != null) {
            return date.toLocalDateTime().truncatedTo(ChronoUnit.MINUTES);
        }
        return null;
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "appointments", ignore = true)
    com.nexym.clinic.domain.availability.model.Availability mapToAvailabilityModel(Long doctorId, AvailabilityRequest availabilityRequest);

    default OffsetDateTime map(LocalDateTime date) {
        if (date != null) {
            return date.atOffset(ZoneOffset.UTC);
        }
        return null;
    }
}
