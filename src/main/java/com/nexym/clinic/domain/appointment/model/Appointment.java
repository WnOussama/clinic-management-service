package com.nexym.clinic.domain.appointment.model;

import com.nexym.clinic.domain.availability.model.Availability;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Appointment {

    private Long id;
    private String prescription;
    private Status status;
    private Boolean cancelled;
    private String cancellationReason;
    private Availability availability;
    private LocalDateTime appointmentDate;
    private LocalDateTime creationDate;

}
