package com.nexym.clinic.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

import java.util.Arrays;

public class CustomLoggingFilter extends CommonsRequestLoggingFilter {

    private final String[] excludedRequests;

    public CustomLoggingFilter(String[] excludedRequests) {
        this.excludedRequests = excludedRequests == null ? new String[0] : excludedRequests;
    }

    @Override
    protected boolean shouldLog(HttpServletRequest request) {
        return Arrays.stream(excludedRequests)
                .filter(excludedRequest -> logger.isInfoEnabled())
                .noneMatch(excludedRequest -> request.getRequestURI().startsWith(request.getContextPath() + excludedRequest));
    }
}
