package com.nexym.clinic.resource.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexym.clinic.ClinicManagementServiceApplication;
import com.nexym.clinic.api.model.AuthenticateRequest;
import com.nexym.clinic.config.ClinicManagementExceptionHandler;
import com.nexym.clinic.domain.user.UserService;
import com.nexym.clinic.domain.user.model.auth.Authentication;
import com.nexym.clinic.domain.user.model.auth.LoginCredential;
import com.nexym.clinic.resource.auth.api.AuthResource;
import com.nexym.clinic.utils.exception.AccessDeniedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = ClinicManagementServiceApplication.class, webEnvironment = RANDOM_PORT)
class AuthResourceTest {

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    @Autowired
    private AuthResource authResource;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuthenticationManager authenticationManager;

    @MockBean
    private UserService userService;

    private MockMvc mockMvc;

    private static final String EMAIL_TEST = "john.doe@mail.com";
    private static final String PASSWORD_TEST = "password";

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(authResource)
                .setControllerAdvice(new ClinicManagementExceptionHandler())
                .apply(SecurityMockMvcConfigurers.springSecurity(springSecurityFilterChain))
                .build();
    }

    @Test
    void should_authenticate_success() throws Exception {
        var loginCredential = LoginCredential.builder()
                .email(EMAIL_TEST)
                .password(PASSWORD_TEST)
                .build();
        var authentication = Authentication
                .builder()
                .id(1L)
                .token("TOTO")
                .expiresIn(17999L)
                .build();
        when(userService.authenticate(loginCredential, authenticationManager)).thenReturn(authentication);
        var authenticateRequest = new AuthenticateRequest()
                .email(EMAIL_TEST)
                .password(PASSWORD_TEST);
        mockMvc.perform(post(authenticateUserUrl())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authenticateRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                            "id": 1,
                            "token": "TOTO",
                            "expiresIn": 17999
                        }
                        """));
    }

    @Test
    void should_authenticate_user_bad_credential_fail() throws Exception {
        when(userService.authenticate(any(), any())).thenThrow(new AccessDeniedException("Access to this resource is denied"));
        var authenticateRequest = new AuthenticateRequest()
                .email(EMAIL_TEST)
                .password(PASSWORD_TEST);
        mockMvc.perform(post(authenticateUserUrl())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authenticateRequest)))
                .andExpect(status().isForbidden());
    }

    private String authenticateUserUrl() {
        return "/api/v1/authenticate";
    }
}
