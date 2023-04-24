package com.nexym.clinic.resource.appointment.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface AppointmentWsMapper {

    default LocalDateTime mapTime(OffsetDateTime date) {
        if (date != null) {
            // truncate only to minutes
            return date.toLocalDateTime().truncatedTo(ChronoUnit.MINUTES);
        }
        return null;
    }
}
