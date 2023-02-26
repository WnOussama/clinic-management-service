package com.nexym.clinic.resource.user.mapper;

import com.nexym.clinic.api.model.User;
import com.nexym.clinic.api.model.UserRequest;
import org.mapstruct.*;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface UserWsMapper {

    User mapToApiModel(com.nexym.clinic.domain.user.model.User userModel);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    com.nexym.clinic.domain.user.model.User mapToUserModel(UserRequest userRequest);

    default OffsetDateTime map(LocalDateTime date) {
        if (date != null) {
            return date.atOffset(ZoneOffset.UTC);
        }
        return null;
    }
}
