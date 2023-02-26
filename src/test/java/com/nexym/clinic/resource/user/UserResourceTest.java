package com.nexym.clinic.resource.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexym.clinic.ClinicManagementServiceApplication;
import com.nexym.clinic.api.model.UserRequest;
import com.nexym.clinic.config.ClinicManagementExceptionHandler;
import com.nexym.clinic.config.security.JwtProvider;
import com.nexym.clinic.domain.user.UserService;
import com.nexym.clinic.resource.user.api.UserResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = ClinicManagementServiceApplication.class, webEnvironment = RANDOM_PORT)
class UserResourceTest {

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    @Autowired
    private UserResource userResource;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    UserService userService;

    @Autowired
    JwtProvider jwtProvider;

    private MockMvc mockMvc;

    private static final String EMAIL_TEST = "john.doe@mail.com";
    private static final String PASSWORD_TEST = "password";

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(userResource)
                .setControllerAdvice(new ClinicManagementExceptionHandler())
                .apply(SecurityMockMvcConfigurers.springSecurity(springSecurityFilterChain))
                .build();
    }

    @Test
    void should_get_user_by_id_not_authenticated_fail() throws Exception {
        mockMvc.perform(get(getUserByIdUrl(2L))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void should_get_user_by_id_success() throws Exception {
        when(userService.loadUserByUsername(EMAIL_TEST)).thenReturn(new User(EMAIL_TEST, PASSWORD_TEST, new ArrayList<>()));
        when(userService.getUserById(1L)).thenReturn(com.nexym.clinic.domain.user.model.User.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .build());
        var token = jwtProvider.generateToken(new User(EMAIL_TEST,
                PASSWORD_TEST,
                new ArrayList<>()));
        mockMvc.perform(get(getUserByIdUrl(1L))
                        .header("Authorization", String.format("Bearer %s", token))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void should_register_user_success() throws Exception {
        when(userService.registerUser(any())).thenReturn(1L);
        var request = new UserRequest().email("john.doe@mail.com")
                .civility(UserRequest.CivilityEnum.MR)
                .phoneNumber("0121348345")
                .firstName("John")
                .lastName("Doe")
                .password("myPassword");
        mockMvc.perform(post(registerUserUrl())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    private String registerUserUrl() {
        return "/api/v1/register";
    }

    private String getUserByIdUrl(Long userId) {
        return String.format("/api/v1/users/%s", userId);
    }
}
