package com.nexym.clinic.infra.patient.mapper;

import com.nexym.clinic.domain.patient.model.Patient;
import com.nexym.clinic.infra.patient.entity.PatientEntity;
import com.nexym.clinic.infra.user.entity.UserEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface PatientEntityMapper {

    @Mapping(target = "appointments", ignore = true)
    @Mapping(target = "user", source = "patientModel")
    PatientEntity mapToEntity(Patient patientModel);

    @Mapping(target = "modifiedDate", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    UserEntity mapToUserEntity(Patient patient);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "civility", source = "user.civility")
    @Mapping(target = "firstName", source = "user.firstName")
    @Mapping(target = "lastName", source = "user.lastName")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "phoneNumber", source = "user.phoneNumber")
    @Mapping(target = "creationDate", source = "user.creationDate")
    Patient mapToModel(PatientEntity patientEntity);
}
