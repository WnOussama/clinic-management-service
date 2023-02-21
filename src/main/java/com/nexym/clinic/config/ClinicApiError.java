package com.nexym.clinic.config;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class ClinicApiError {

    private String error;
    private String path;
    private Integer status;
    private LocalDateTime timestamp;
    private String message;
    @Builder.Default
    private List<String> subErrors = new ArrayList<>();


}
