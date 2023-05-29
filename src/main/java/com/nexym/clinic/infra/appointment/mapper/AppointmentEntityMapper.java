package com.nexym.clinic.infra.appointment.mapper;

import com.nexym.clinic.domain.appointment.model.Appointment;
import com.nexym.clinic.domain.availability.model.Availability;
import com.nexym.clinic.infra.appointment.entity.AppointmentEntity;
import com.nexym.clinic.infra.availability.entity.AvailabilityEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface AppointmentEntityMapper {

    @Mapping(target = "patientId", source = "patient.id")
    @Mapping(target = "doctorId", source = "availability.doctor.id")
    Appointment mapToModel(AppointmentEntity appointmentEntity);

    @Mapping(target = "patient", ignore = true)
    @Mapping(target = "modifiedDate", ignore = true)
    AppointmentEntity mapToEntity(Appointment appointmentModel);

    @Mapping(target = "doctor", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "modifiedDate", ignore = true)
    AvailabilityEntity mapToAvailabilityEntity(Availability availability);

    List<Appointment> mapToModelList(List<AppointmentEntity> appointmentList);
}
