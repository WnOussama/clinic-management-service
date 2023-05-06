package com.nexym.clinic.resource.availability;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexym.clinic.ClinicManagementServiceApplication;
import com.nexym.clinic.api.model.AvailabilityRequest;
import com.nexym.clinic.config.ClinicManagementExceptionHandler;
import com.nexym.clinic.domain.availability.AvailabilityService;
import com.nexym.clinic.resource.availability.api.AvailabilityResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.OffsetDateTime;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = ClinicManagementServiceApplication.class, webEnvironment = RANDOM_PORT)
class AvailabilityResourceTest {

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AvailabilityService availabilityService;

    @Autowired
    private AvailabilityResource availabilityResource;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(availabilityResource)
                .setControllerAdvice(new ClinicManagementExceptionHandler())
                .apply(SecurityMockMvcConfigurers.springSecurity(springSecurityFilterChain))
                .build();
    }

    @Test
    void should_add_new_availability_not_authorized_fail() throws Exception {
        var availabilityRequest = new AvailabilityRequest()
                .startDate(OffsetDateTime.now())
                .endDate(OffsetDateTime.now().plusDays(1));
        mockMvc.perform(post(addNewAvailabilityUri())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(availabilityRequest)))
                .andExpect(status().isUnauthorized());
    }

    private String addNewAvailabilityUri() {
        return "/api/v1/doctors/1/availabilities";
    }
}

