package com.nexym.clinic.infra.user.mapper;

import com.nexym.clinic.domain.doctor.model.Doctor;
import com.nexym.clinic.domain.user.model.User;
import com.nexym.clinic.infra.user.entity.UserEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface UserEntityMapper {

    User mapToModel(UserEntity userEntity);

    @Mapping(target = "modifiedDate", ignore = true)
    UserEntity mapToEntity(User userModel);

    @Mapping(target = "modifiedDate", ignore = true)
    UserEntity mapToUser(Doctor doctor);

    List<User> mapToModelList(List<UserEntity> userList);
}
