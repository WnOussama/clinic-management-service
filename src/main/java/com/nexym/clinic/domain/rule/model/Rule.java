package com.nexym.clinic.domain.rule.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Rule {

    private Long id;
    private Long startHour;
    private Long endHour;
    private Long startBreakHour;
    private Long endBreakHour;
}
