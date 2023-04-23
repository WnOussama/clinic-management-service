package com.nexym.clinic.domain.rule.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Rule {

    private Long id;
    private Integer startHour;
    private Integer endHour;
    private Integer startBreakHour;
    private Integer endBreakHour;
}
