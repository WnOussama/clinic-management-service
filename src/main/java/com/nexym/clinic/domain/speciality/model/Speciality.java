package com.nexym.clinic.domain.speciality.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Speciality {

    private Long id;
    private String name;
    private Long appointmentDuration;
}
