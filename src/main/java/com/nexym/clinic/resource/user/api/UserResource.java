package com.nexym.clinic.resource.user.api;

import com.nexym.clinic.api.UsersApi;
import com.nexym.clinic.api.model.User;
import com.nexym.clinic.domain.user.UserService;
import com.nexym.clinic.resource.user.mapper.UserWsMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
public class UserResource implements UsersApi {

    private final UserWsMapper userWsMapper;

    private final UserService userService;

    @Override
    public ResponseEntity<List<User>> getUserList() {
        return ResponseEntity.ok(userWsMapper.mapToUserResponseList(userService.getUserList()));
    }
}
