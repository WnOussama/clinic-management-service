package com.nexym.clinic.domain.doctor.mapper;

import com.nexym.clinic.domain.doctor.model.Doctor;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface DoctorMapper {

    @Mapping(target = "id", ignore = true)
    void update(@MappingTarget Doctor existingDoctor, Doctor request);
}
