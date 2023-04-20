package com.nexym.clinic.resource.patient.mapper;

import com.nexym.clinic.api.model.PatientRequest;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface PatientWsMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    com.nexym.clinic.domain.patient.model.Patient mapToPatientModel(PatientRequest patientRequest);
}
