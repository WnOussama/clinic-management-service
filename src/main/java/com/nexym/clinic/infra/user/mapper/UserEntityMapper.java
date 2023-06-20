package com.nexym.clinic.infra.user.mapper;

import com.nexym.clinic.domain.doctor.model.Doctor;
import com.nexym.clinic.domain.user.model.ResetPassword;
import com.nexym.clinic.domain.user.model.User;
import com.nexym.clinic.infra.user.entity.ResetPasswordEntity;
import com.nexym.clinic.infra.user.entity.UserEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface UserEntityMapper {

    @Mapping(target = "id", source = "reset.id")
    @Mapping(target = "userId", source = "id")
    @Mapping(target = "expiryDate", source = "reset.expiryDate")
    @Mapping(target = "token", source = "reset.token")
    ResetPassword map(UserEntity userEntity);

    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "modifiedDate", ignore = true)
    @Mapping(target = "user.id", source = "userId")
    ResetPasswordEntity map(ResetPassword resetPassword);

    @Mapping(target = "userId", source = "id")
    @Mapping(target = "reset", source = "userEntity", conditionExpression = "java(userEntity.getReset() != null)")
    User mapToModel(UserEntity userEntity);

    @Mapping(target = "id", source = "userId")
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "modifiedDate", ignore = true)
    UserEntity mapToEntity(User userModel);

    List<User> mapToModelList(List<UserEntity> userList);
}
