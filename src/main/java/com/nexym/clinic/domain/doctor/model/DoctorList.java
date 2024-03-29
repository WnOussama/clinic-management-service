package com.nexym.clinic.domain.doctor.model;

import com.nexym.clinic.domain.common.PageModel;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class DoctorList extends PageModel<Doctor> {

    @SuppressWarnings({"java:S107", "constructor not too much complex"})
    @Builder
    public DoctorList(List<Doctor> items,
                      Integer totalPages,
                      Boolean first,
                      Boolean last,
                      Long totalElements,
                      Integer numberOfElements,
                      Integer size,
                      Integer number) {
        super(items, totalPages, first, last, totalElements, numberOfElements, size, number);
    }
}
