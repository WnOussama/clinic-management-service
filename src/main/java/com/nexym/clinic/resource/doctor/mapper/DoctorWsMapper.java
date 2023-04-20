package com.nexym.clinic.resource.doctor.mapper;

import com.nexym.clinic.api.model.DoctorListResponse;
import com.nexym.clinic.api.model.DoctorRequest;
import com.nexym.clinic.domain.doctor.model.DoctorList;
import org.mapstruct.*;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface DoctorWsMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "ruleId", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "availabilities", ignore = true)
    com.nexym.clinic.domain.doctor.model.Doctor mapToDoctorModel(DoctorRequest doctorRequest);

    default OffsetDateTime map(LocalDateTime date) {
        if (date != null) {
            return date.atOffset(ZoneOffset.UTC);
        }
        return null;
    }


    DoctorListResponse mapToDoctorResponseList(DoctorList doctorList);
}
