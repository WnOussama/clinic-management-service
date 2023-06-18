package com.nexym.clinic.resource.appointment.mapper;

import com.nexym.clinic.api.model.Appointment;
import org.mapstruct.*;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;

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

    default OffsetDateTime map(LocalDateTime date) {
        if (date != null) {
            return date.atOffset(ZoneOffset.UTC);
        }
        return null;
    }

    @Mapping(target = "availabilityId", source = "availabilityId")
    Appointment map(com.nexym.clinic.domain.appointment.model.Appointment appointment);

    List<Appointment> mapToList(List<com.nexym.clinic.domain.appointment.model.Appointment> appointmentList);
}
