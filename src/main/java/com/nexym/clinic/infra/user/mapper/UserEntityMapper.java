package com.nexym.clinic.infra.user.mapper;

import com.nexym.clinic.domain.user.model.User;
import com.nexym.clinic.infra.user.entity.UserEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface UserEntityMapper {

    User mapToModel(UserEntity userEntity);

    @Mapping(target = "modifiedDate", ignore = true)
    UserEntity mapToEntity(User userModel);
}
