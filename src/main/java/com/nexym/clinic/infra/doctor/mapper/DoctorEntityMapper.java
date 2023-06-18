package com.nexym.clinic.infra.doctor.mapper;

import com.nexym.clinic.domain.appointment.model.Appointment;
import com.nexym.clinic.domain.availability.model.Availability;
import com.nexym.clinic.domain.bill.model.Bill;
import com.nexym.clinic.domain.doctor.model.Doctor;
import com.nexym.clinic.domain.doctor.model.DoctorList;
import com.nexym.clinic.infra.appointment.entity.AppointmentEntity;
import com.nexym.clinic.infra.availability.entity.AvailabilityEntity;
import com.nexym.clinic.infra.bill.entity.BillEntity;
import com.nexym.clinic.infra.doctor.entity.DoctorEntity;
import com.nexym.clinic.infra.speciality.entity.SpecialityEntity;
import com.nexym.clinic.infra.user.entity.UserEntity;
import org.mapstruct.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface DoctorEntityMapper {

    @Mapping(target = "user", source = "doctorModel")
    @Mapping(target = "availabilities", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "rule.id", source = "ruleId")
    @Mapping(target = "speciality.id", source = "specialityId")
    DoctorEntity mapToEntity(Doctor doctorModel);

    @Mapping(target = "id", ignore = true)
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

    @Mapping(target = "patientId", source = "patient.id")
    @Mapping(target = "availabilityId", source = "availability.id")
    @Mapping(target = "doctorId", source = "availability.doctor.id")
    Appointment mapToModel(AppointmentEntity appointmentEntity);

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

    @Mapping(target = "patient", ignore = true)
    @Mapping(target = "availability.id", source = "availabilityId")
    @Mapping(target = "modifiedDate", ignore = true)
    AppointmentEntity map(Appointment appointment);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "doctor.id", source = "doctorId")
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "modifiedDate", ignore = true)
    AvailabilityEntity map(Availability availability);

    @Mapping(target = "doctorId", source = "doctor.id")
    Availability mapToModel(AvailabilityEntity availabilityEntity);

    @Mapping(target = "doctor.id", source = "doctorId")
    @Mapping(target = "appointment.id", source = "appointmentId")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "modifiedDate", ignore = true)
    BillEntity map(Bill bill);

    @Mapping(target = "appointmentId", source = "appointment.id")
    @Mapping(target = "appointmentFee", source = "doctor.speciality.appointmentFee")
    @Mapping(target = "doctorId", source = "doctor.id")
    Bill mapToModel(BillEntity billEntity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "rule", ignore = true)
    @Mapping(target = "user.id", ignore = true)
    @Mapping(target = "user.creationDate", ignore = true)
    @Mapping(target = "user.modifiedDate", ignore = true)
    @Mapping(target = "availabilities", ignore = true)
    @Mapping(target = "speciality", source = "specialityId")
    @Mapping(target = "user.firstName", source = "firstName")
    @Mapping(target = "user.lastName", source = "lastName")
    @Mapping(target = "user.civility", source = "civility")
    @Mapping(target = "user.email", source = "email")
    @Mapping(target = "user.password", source = "password")
    @Mapping(target = "user.phoneNumber", source = "phoneNumber")
    void update(@MappingTarget DoctorEntity existingDoctor, Doctor updateRequest);

    default SpecialityEntity map(Long specialityId) {
        var speciality = new SpecialityEntity();
        speciality.setId(specialityId);
        return speciality;
    }
}
