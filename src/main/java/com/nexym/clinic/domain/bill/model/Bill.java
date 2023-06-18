package com.nexym.clinic.domain.bill.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
public class Bill implements Serializable {
    private Long id;
    private BillStatus status;
    private Long appointmentFee;
    private Long appointmentId;
    private Long doctorId;
    private LocalDateTime creationDate;
}
