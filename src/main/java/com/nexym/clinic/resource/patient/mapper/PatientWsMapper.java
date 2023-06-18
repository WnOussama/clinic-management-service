package com.nexym.clinic.resource.patient.mapper;

import com.nexym.clinic.api.model.Appointment;
import com.nexym.clinic.api.model.Patient;
import com.nexym.clinic.api.model.PatientListResponse;
import com.nexym.clinic.api.model.PatientRequest;
import com.nexym.clinic.domain.patient.model.PatientList;
import org.mapstruct.*;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface PatientWsMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "appointments", ignore = true)
    com.nexym.clinic.domain.patient.model.Patient mapToPatientModel(PatientRequest patientRequest);

    default OffsetDateTime map(LocalDateTime date) {
        if (date != null) {
            return date.atOffset(ZoneOffset.UTC);
        }
        return null;
    }

    @Mapping(target = "availabilityId", source = "availabilityId")
    Appointment map(com.nexym.clinic.domain.appointment.model.Appointment appointment);

    Patient map(com.nexym.clinic.domain.patient.model.Patient patientModel);

    PatientListResponse mapToPatientResponseList(PatientList patientList);
}