package com.nexym.clinic.resource.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexym.clinic.ClinicManagementServiceApplication;
import com.nexym.clinic.api.model.UserRequest;
import com.nexym.clinic.config.ClinicManagementExceptionHandler;
import com.nexym.clinic.config.security.JwtProvider;
import com.nexym.clinic.domain.user.UserService;
import com.nexym.clinic.domain.user.model.UserRole;
import com.nexym.clinic.resource.user.api.UserResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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
    private UserService userService;

    @Autowired
    private JwtProvider jwtProvider;

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
        mockMvc.perform(get(usersByIdUrl(2L))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void should_update_user_by_id_not_authenticated_fail() throws Exception {
        mockMvc.perform(put(usersByIdUrl(2L))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void should_delete_user_by_id_not_authenticated_fail() throws Exception {
        mockMvc.perform(delete(usersByIdUrl(2L))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void should_delete_user_by_id_success() throws Exception {
        when(userService.loadUserByUsername(EMAIL_TEST)).thenReturn(new User(EMAIL_TEST, PASSWORD_TEST, new ArrayList<>()));
        Mockito.doNothing().when(userService).deleteUserById(2L);
        var token = jwtProvider.generateToken(new User(EMAIL_TEST,
                PASSWORD_TEST,
                new ArrayList<>()));
        mockMvc.perform(delete(usersByIdUrl(2L))
                        .header("Authorization", String.format("Bearer %s", token))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
        verify(userService, times(1)).deleteUserById(2L);
    }

    @Test
    void should_update_user_by_id_success() throws Exception {
        when(userService.loadUserByUsername(EMAIL_TEST)).thenReturn(new User(EMAIL_TEST, PASSWORD_TEST, new ArrayList<>()));
        when(userService.updateUserById(eq(2L), any(com.nexym.clinic.domain.user.model.User.class))).thenReturn(com.nexym.clinic.domain.user.model.User.builder()
                .id(2L)
                .firstName("John")
                .lastName("Doe")
                .role(UserRole.PATIENT)
                .build());
        var token = jwtProvider.generateToken(new User(EMAIL_TEST,
                PASSWORD_TEST,
                new ArrayList<>()));
        mockMvc.perform(put(usersByIdUrl(2L))
                        .content("{\"firstName\": \"toto\"}")
                        .header("Authorization", String.format("Bearer %s", token))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                            "id":2,
                            "civility":null,
                            "firstName":"John",
                            "lastName":"Doe",
                            "role":"PATIENT",
                            "email":null,
                            "phoneNumber":null,
                            "creationDate":null}
                        """));
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
        mockMvc.perform(get(usersByIdUrl(1L))
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
                .role(UserRequest.RoleEnum.PATIENT)
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

    private String usersByIdUrl(Long userId) {
        return String.format("/api/v1/users/%s", userId);
    }
}
