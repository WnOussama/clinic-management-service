package com.nexym.clinic.domain.doctor.mapper;

import com.nexym.clinic.domain.doctor.model.Doctor;
import com.nexym.clinic.domain.user.model.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface DoctorMapper {

    User mapToUser(Doctor doctor);

}
