package com.nexym.clinic.infra.doctor.mapper;

import com.nexym.clinic.domain.doctor.model.Doctor;
import com.nexym.clinic.domain.doctor.model.DoctorList;
import com.nexym.clinic.infra.doctor.entity.DoctorEntity;
import com.nexym.clinic.infra.user.entity.UserEntity;
import org.mapstruct.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface DoctorEntityMapper {

    @Mapping(target = "user", source = "doctorModel")
    @Mapping(target = "availabilities", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "rule.id", source = "ruleId")
    @Mapping(target = "speciality.id", source = "specialityId")
    DoctorEntity mapToEntity(Doctor doctorModel);

    @Mapping(target = "modifiedDate", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    UserEntity mapToUserEntity(Doctor doctorModel);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "specialityId", source = "speciality.id")
    @Mapping(target = "ruleId", source = "rule.id")
    @Mapping(target = "civility", source = "user.civility")
    @Mapping(target = "firstName", source = "user.firstName")
    @Mapping(target = "lastName", source = "user.lastName")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "phoneNumber", source = "user.phoneNumber")
    @Mapping(target = "creationDate", source = "user.creationDate")
    Doctor mapToModel(DoctorEntity doctorEntity);

    List<Doctor> mapToModelList(List<DoctorEntity> doctorList);

    default DoctorList mapToModelList(Page<DoctorEntity> page) {
        var items = page.get()
                .map(this::mapToModel)
                .toList();
        return DoctorList.builder()
                .items(items)
                .first(page.isFirst())
                .last(page.isLast())
                .number(page.getNumber())
                .size(page.getSize())
                .numberOfElements(page.getNumberOfElements())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }

}
