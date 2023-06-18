package com.nexym.clinic.infra.bill.mapper;

import com.nexym.clinic.domain.appointment.model.Appointment;
import com.nexym.clinic.domain.bill.model.Bill;
import com.nexym.clinic.infra.appointment.entity.AppointmentEntity;
import com.nexym.clinic.infra.bill.entity.BillEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface BillEntityMapper {

    @Mapping(target = "patient", ignore = true)
    @Mapping(target = "availability.id", source = "availabilityId")
    @Mapping(target = "modifiedDate", ignore = true)
    AppointmentEntity map(Appointment appointment);

    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "modifiedDate", ignore = true)
    @Mapping(target = "appointment.id", source = "appointmentId")
    @Mapping(target = "doctor.id", source = "doctorId")
    BillEntity mapToEntity(Bill newBill);
}
