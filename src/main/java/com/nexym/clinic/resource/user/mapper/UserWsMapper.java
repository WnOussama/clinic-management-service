package com.nexym.clinic.resource.user.mapper;

import com.nexym.clinic.api.model.AuthenticateRequest;
import com.nexym.clinic.api.model.AuthenticateResponse;
import com.nexym.clinic.api.model.User;
import com.nexym.clinic.api.model.UserRequest;
import com.nexym.clinic.domain.user.model.auth.Authentication;
import com.nexym.clinic.domain.user.model.auth.LoginCredential;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface UserWsMapper {

    @Mapping(target = "id", source = "userId")
    User mapToApiModel(com.nexym.clinic.domain.user.model.User userModel);

    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    com.nexym.clinic.domain.user.model.User mapToUserModel(UserRequest userRequest);

    LoginCredential mapToCredentials(AuthenticateRequest authenticateRequest);

    List<User> mapToUserResponseList(List<com.nexym.clinic.domain.user.model.User> userList);


    default AuthenticateResponse mapToAuthenticateResponse(Authentication authenticate) {
        if (authenticate == null) {
            return null;
        }

        AuthenticateResponse response = new AuthenticateResponse();
        response.setToken(authenticate.getToken());
        response.setExpiresIn(authenticate.getExpiresIn());
        response.setIsDoctor(authenticate.isDoctor());
        response.setId(authenticate.getId());

        return response;
    }
}
