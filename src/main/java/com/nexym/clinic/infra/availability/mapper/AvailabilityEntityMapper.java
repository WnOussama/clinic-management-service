package com.nexym.clinic.infra.availability.mapper;

import com.nexym.clinic.domain.appointment.model.Appointment;
import com.nexym.clinic.domain.availability.model.Availability;
import com.nexym.clinic.domain.bill.model.Bill;
import com.nexym.clinic.infra.appointment.entity.AppointmentEntity;
import com.nexym.clinic.infra.availability.entity.AvailabilityEntity;
import com.nexym.clinic.infra.bill.entity.BillEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface AvailabilityEntityMapper {

    @Mapping(target = "appointmentFee", source = "doctor.speciality.appointmentFee")
    @Mapping(target = "appointmentId", source = "appointment.id")
    @Mapping(target = "doctorId", source = "doctor.id")
    Bill mapToBillModel(BillEntity billEntity);

    @Mapping(target = "patientId", source = "patient.id")
    @Mapping(target = "availabilityId", source = "availability.id")
    @Mapping(target = "doctorId", source = "availability.doctor.id")
    Appointment mapToAppointmentModel(AppointmentEntity appointmentEntity);

    @Mapping(target = "doctorId", source = "doctor.id")
    Availability mapToModel(AvailabilityEntity availabilityEntity);

    @Mapping(target = "appointment", ignore = true)
    @Mapping(target = "doctor", ignore = true)
    @Mapping(target = "modifiedDate", ignore = true)
    BillEntity mapToBillEntity(Bill bill);

    @Mapping(target = "patient", ignore = true)
    @Mapping(target = "availability.id", source = "availabilityId")
    @Mapping(target = "modifiedDate", ignore = true)
    AppointmentEntity mapToAppointmentEntity(Appointment appointment);

    @Mapping(target = "doctor", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "modifiedDate", ignore = true)
    AvailabilityEntity mapToEntity(Availability availabilityModel);

    List<Availability> mapToModelList(List<AvailabilityEntity> availabilityList);
}
