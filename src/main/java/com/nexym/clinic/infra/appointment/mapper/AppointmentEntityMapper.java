package com.nexym.clinic.infra.appointment.mapper;

import com.nexym.clinic.domain.appointment.model.Appointment;
import com.nexym.clinic.domain.bill.model.Bill;
import com.nexym.clinic.infra.appointment.entity.AppointmentEntity;
import com.nexym.clinic.infra.bill.entity.BillEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface AppointmentEntityMapper {

    @Mapping(target = "modifiedDate", ignore = true)
    @Mapping(target = "appointment.id", source = "appointmentId")
    @Mapping(target = "doctor.id", source = "doctorId")
    BillEntity map(Bill billModel);

    @Mapping(target = "appointmentId", source = "appointment.id")
    @Mapping(target = "doctorId", source = "doctor.id")
    @Mapping(target = "appointmentFee", ignore = true)
    Bill mapToModel(BillEntity billEntity);

    @Mapping(target = "patientId", source = "patient.id")
    @Mapping(target = "availabilityId", source = "availability.id")
    @Mapping(target = "doctorId", source = "availability.doctor.id")
    Appointment mapToModel(AppointmentEntity appointmentEntity);

    @Mapping(target = "patient.id", source = "patientId")
    @Mapping(target = "availability.id", source = "availabilityId")
    @Mapping(target = "modifiedDate", ignore = true)
    AppointmentEntity mapToEntity(Appointment appointmentModel);

    List<Appointment> mapToModelList(List<AppointmentEntity> appointmentList);
}
