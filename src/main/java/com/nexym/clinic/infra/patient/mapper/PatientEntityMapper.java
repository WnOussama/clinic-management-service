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

    @Mapping(target = "user", source = "userId")
    @Mapping(target = "appointments", ignore = true)
    PatientEntity mapToEntity(Patient patientModel);

    default UserEntity mapToUserEntity(Long id) {
        var user = new UserEntity();
        user.setId(id);
        return user;
    }

}
