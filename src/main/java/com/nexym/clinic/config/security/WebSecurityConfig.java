package com.nexym.clinic.config.security;

import com.nexym.clinic.domain.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@SuppressWarnings({"java:S3305", "inject directly the field value will not add any value"})
public class WebSecurityConfig {

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    private static final String ACTUATOR_ENDPOINT = "/actuator/health";
    private static final String DOCTORS_ENDPOINT = "/api/v1/doctors";
    private static final String PATIENTS_ENDPOINT = "/api/v1/patients";
    private static final String DOCTOR_SPECIALITIES_ENDPOINT = "/api/v1/specialities";
    private static final String AUTHENTICATE_ENDPOINT = "/api/v1/authenticate";
    private static final String[] AUTH_WHITELIST = {
            ACTUATOR_ENDPOINT,
            DOCTOR_SPECIALITIES_ENDPOINT,
            AUTHENTICATE_ENDPOINT,
            // -- Swagger UI v3 (OpenAPI)
            "/api-docs/**",
            "/swagger-ui/**"
            // other public endpoints of your API may be appended to this array
    };

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth ->
                        // do not authenticate this particular request
                        auth.requestMatchers(AUTH_WHITELIST).permitAll()
                                // require authentication for GET requests to PATIENT_ENDPOINT
                                .requestMatchers(HttpMethod.GET, PATIENTS_ENDPOINT).authenticated()
                                // require authentication for GET requests to DOCTOR_ENDPOINT
                                .requestMatchers(HttpMethod.GET, DOCTORS_ENDPOINT).authenticated()
                                // require authentication for POST requests to PATIENT_ENDPOINT
                                .requestMatchers(HttpMethod.POST, PATIENTS_ENDPOINT).permitAll()
                                // require authentication for POST requests to DOCTOR_ENDPOINT
                                .requestMatchers(HttpMethod.POST, DOCTORS_ENDPOINT).permitAll()
                                // all other requests need to be authenticated
                                .anyRequest().authenticated())
                // make sure we use stateless session; session won't be used to
                // store user's state.
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // Add a filter to validate the tokens with every request
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .userDetailsService(userService)
                .build();
    }
}
