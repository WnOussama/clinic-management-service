package com.nexym.clinic.resource.doctor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexym.clinic.ClinicManagementServiceApplication;
import com.nexym.clinic.api.model.DoctorRequest;
import com.nexym.clinic.config.ClinicManagementExceptionHandler;
import com.nexym.clinic.domain.doctor.DoctorService;
import com.nexym.clinic.resource.doctor.api.DoctorResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = ClinicManagementServiceApplication.class, webEnvironment = RANDOM_PORT)
class DoctorResourceTest {

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DoctorService doctorService;

    @Autowired
    private DoctorResource doctorResource;

    private MockMvc mockMvc;

    private static final String EMAIL_TEST = "john.doe@mail.com";
    private static final String PASSWORD_TEST = "password";

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(doctorResource)
                .setControllerAdvice(new ClinicManagementExceptionHandler())
                .apply(SecurityMockMvcConfigurers.springSecurity(springSecurityFilterChain))
                .build();
    }

    @Test
    void should_register_doctor_success() throws Exception {
        when(doctorService.registerDoctor(any())).thenReturn(2L);
        var request = new DoctorRequest().email("jessie.doe@mail.com")
                .civility(DoctorRequest.CivilityEnum.MR)
                .phoneNumber("01122334455")
                .firstName("Jessie")
                .lastName("Doe")
                .specialityId(1L)
                .iban("FR7630001007941234567890185")
                .password("myPassword");
        mockMvc.perform(post(registerDoctorUrl())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    private String registerDoctorUrl() {
        return "/api/v1/doctors";
    }
}
