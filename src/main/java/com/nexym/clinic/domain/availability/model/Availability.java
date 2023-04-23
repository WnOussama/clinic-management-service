package com.nexym.clinic.domain.availability.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class Availability implements Serializable {

    private Long id;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public List<String> applyValidations() {
        List<String> subErrors = new ArrayList<>();
        if (getStartDate() == null) {
            subErrors.add("Start date should be filled");
        }
        if (getEndDate() == null) {
            subErrors.add("End date should be filled");
        }
        return subErrors;
    }
}
