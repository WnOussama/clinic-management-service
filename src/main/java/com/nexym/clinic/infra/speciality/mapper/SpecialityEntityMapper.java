package com.nexym.clinic.infra.speciality.mapper;

import com.nexym.clinic.domain.speciality.model.Speciality;
import com.nexym.clinic.infra.speciality.entity.SpecialityEntity;
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
public interface SpecialityEntityMapper {

    List<Speciality> mapToModelList(List<SpecialityEntity> specialityList);

    Speciality map(SpecialityEntity specialityEntity);
}
