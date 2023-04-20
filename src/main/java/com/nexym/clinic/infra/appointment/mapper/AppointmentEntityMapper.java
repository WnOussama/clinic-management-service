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

    Appointment mapToModel(AppointmentEntity appointmentEntity);

    @Mapping(target = "modifiedDate", ignore = true)
    AppointmentEntity mapToEntity(Appointment appointmentModel);

    @Mapping(target = "modifiedDate", ignore = true)
    AvailabilityEntity mapToAvailabilityEntity(Availability availability);

    List<Appointment> mapToModelList(List<AppointmentEntity> appointmentList);
}
