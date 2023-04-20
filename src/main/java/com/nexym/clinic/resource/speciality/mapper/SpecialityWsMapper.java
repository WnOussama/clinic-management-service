package com.nexym.clinic.resource.speciality.mapper;

import com.nexym.clinic.api.model.Speciality;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface SpecialityWsMapper {

    List<Speciality> mapToSpecialityResponseList(List<com.nexym.clinic.domain.speciality.model.Speciality> specialityList);
}
