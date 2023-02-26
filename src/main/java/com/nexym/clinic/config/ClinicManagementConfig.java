package com.nexym.clinic.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@Configuration
public class ClinicManagementConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CommonsRequestLoggingFilter logRequests(@Value("${logging.excluded-requests}") String[] excludedRequests) {
        CommonsRequestLoggingFilter filter = new CustomLoggingFilter(excludedRequests);
        filter.setIncludeQueryString(true);
        filter.setIncludePayload(true);
        filter.setMaxPayloadLength(2000);
        filter.setIncludeHeaders(false);
        return filter;
    }
}
