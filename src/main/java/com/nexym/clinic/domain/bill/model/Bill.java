package com.nexym.clinic.domain.bill.model;

import com.nexym.clinic.domain.appointment.model.Status;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
public class Bill implements Serializable {
    private Long id;
    private Status status;
    private Long appointmentFee;
    private LocalDateTime creationDate;
}
