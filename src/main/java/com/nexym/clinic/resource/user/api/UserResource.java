package com.nexym.clinic.resource.user.api;

import com.nexym.clinic.api.UsersApi;
import com.nexym.clinic.api.model.AuthenticateRequest;
import com.nexym.clinic.api.model.AuthenticateResponse;
import com.nexym.clinic.api.model.User;
import com.nexym.clinic.api.model.UserRequest;
import com.nexym.clinic.config.security.JwtProvider;
import com.nexym.clinic.domain.user.UserService;
import com.nexym.clinic.resource.user.mapper.UserWsMapper;
import com.nexym.clinic.utils.exception.FunctionalException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.ArrayList;
import java.util.Date;

@RestController
@Slf4j
@AllArgsConstructor
public class UserResource implements UsersApi {

    private final UserWsMapper userWsMapper;

    private final UserService userService;

    private final AuthenticationManager authenticationManager;

    private final JwtProvider jwtUtils;

    @Override
    public ResponseEntity<User> getUserById(Long userId) {
        return ResponseEntity.ok(userWsMapper.mapToApiModel(userService.getUserById(userId)));
    }

    @Override
    public ResponseEntity<Void> registerUser(UserRequest userRequest) {
        var savedUserId = userService.registerUser(userWsMapper.mapToUserModel(userRequest));
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedUserId).toUri()).build();
    }

    @Override
    public ResponseEntity<AuthenticateResponse> authenticate(AuthenticateRequest authenticateRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticateRequest.getEmail(),
                    authenticateRequest.getPassword(),
                    new ArrayList<>()));
            var user = userService.loadUserByUsername(authenticateRequest.getEmail());
            if (user != null) {
                var token = jwtUtils.generateToken(user);
                var expirationDate = jwtUtils.getExpirationDateFromToken(token);
                var now = new Date();
                var response = new AuthenticateResponse();
                response.setToken(token);
                response.setExpiresIn((expirationDate.getTime() - now.getTime()) / 1000);
                return ResponseEntity.ok(response);
            }
            throw new FunctionalException("user does not exist");
        } catch (Exception e) {
            throw new FunctionalException(e.getMessage());
        }
    }
}
