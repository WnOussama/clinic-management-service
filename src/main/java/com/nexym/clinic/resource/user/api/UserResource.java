package com.nexym.clinic.resource.user.api;

import com.nexym.clinic.api.UsersApi;
import com.nexym.clinic.api.model.User;
import com.nexym.clinic.api.model.UserRequest;
import com.nexym.clinic.domain.user.UserService;
import com.nexym.clinic.resource.user.mapper.UserWsMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@Slf4j
@AllArgsConstructor
public class UserResource implements UsersApi {

    private final UserService userService;
    private final UserWsMapper userWsMapper;

    @Override
    public ResponseEntity<User> getUserById(Long userId) {
        return ResponseEntity.ok(userWsMapper.mapToApiModel(userService.getUserById(userId)));
    }

    @Override
    public ResponseEntity<Void> registerUser(UserRequest userRequest) {
        var savedUserId = userService.registerUser(userWsMapper.mapToUserModel(userRequest));
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedUserId).toUri()).build();
    }
}
