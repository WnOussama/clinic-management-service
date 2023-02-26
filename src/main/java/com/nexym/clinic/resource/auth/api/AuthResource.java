package com.nexym.clinic.resource.auth.api;

import com.nexym.clinic.api.AuthApi;
import com.nexym.clinic.api.model.AuthenticateRequest;
import com.nexym.clinic.api.model.AuthenticateResponse;
import com.nexym.clinic.domain.user.UserService;
import com.nexym.clinic.resource.user.mapper.UserWsMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@AllArgsConstructor
public class AuthResource implements AuthApi {

    private final UserWsMapper userWsMapper;

    private final UserService userService;

    private final AuthenticationManager authenticationManager;

    @Override
    public ResponseEntity<AuthenticateResponse> authenticate(AuthenticateRequest authenticateRequest) {
        return ResponseEntity.ok(userWsMapper.mapToAuthenticateResponse(userService.authenticate(userWsMapper.mapToCredentials(authenticateRequest),
                authenticationManager)));
    }
}
