package com.nexym.clinic.domain.common;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public abstract class PageModel<T> {
    List<T> items;
    Integer totalPages;
    Boolean first;
    Boolean last;
    Long totalElements;
    Integer numberOfElements;
    Integer size;
    Integer number;
}
