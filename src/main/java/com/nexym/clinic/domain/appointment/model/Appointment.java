package com.nexym.clinic.domain.appointment.model;

import com.nexym.clinic.domain.bill.model.Bill;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
public class Appointment implements Serializable {

    private Long id;
    private Long patientId;
    private Long doctorId;
    private String prescription;
    private AppointmentStatus status;
    private String cancellationReason;
    private Long availabilityId;
    private Bill bill;
    private LocalDateTime appointmentDate;
    private LocalDateTime creationDate;

}
