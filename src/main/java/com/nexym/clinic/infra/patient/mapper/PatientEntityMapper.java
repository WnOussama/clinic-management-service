package com.nexym.clinic.infra.patient.mapper;

import com.nexym.clinic.domain.appointment.model.Appointment;
import com.nexym.clinic.domain.availability.model.Availability;
import com.nexym.clinic.domain.patient.model.Patient;
import com.nexym.clinic.domain.patient.model.PatientList;
import com.nexym.clinic.infra.appointment.entity.AppointmentEntity;
import com.nexym.clinic.infra.availability.entity.AvailabilityEntity;
import com.nexym.clinic.infra.patient.entity.PatientEntity;
import com.nexym.clinic.infra.user.entity.UserEntity;
import org.mapstruct.*;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
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

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "patient", ignore = true)
    @Mapping(target = "patient.id", source = "patientId")
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "modifiedDate", ignore = true)
    AppointmentEntity mapToAppointmentEntity(Appointment appointment);

    @Mapping(target = "patientId", source = "patient.id")
    @Mapping(target = "doctorId", source = "availability.doctor.id")
    Appointment mapToAppointment(AppointmentEntity appointmentEntity);

    @Mapping(target = "doctor", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "modifiedDate", ignore = true)
    AvailabilityEntity mapToAvailabilityEntity(Availability availability);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user.id", ignore = true)
    @Mapping(target = "user.creationDate", ignore = true)
    @Mapping(target = "user.modifiedDate", ignore = true)
    @Mapping(target = "user.firstName", source = "firstName")
    @Mapping(target = "user.lastName", source = "lastName")
    @Mapping(target = "user.civility", source = "civility")
    @Mapping(target = "user.email", source = "email")
    @Mapping(target = "user.password", source = "password")
    @Mapping(target = "user.phoneNumber", source = "phoneNumber")
    void update(@MappingTarget PatientEntity existingPatient, Patient updateRequest);

    default PatientList mapToModelList(Page<PatientEntity> page) {
        var items = page.get()
                .map(this::mapToModel)
                .toList();
        return PatientList.builder()
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
