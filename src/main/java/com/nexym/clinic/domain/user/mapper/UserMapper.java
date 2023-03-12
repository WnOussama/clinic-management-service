package com.nexym.clinic.domain.user.mapper;

import com.nexym.clinic.domain.user.model.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface UserMapper {


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    void merge(@MappingTarget User userFromDb, User userRequest);

}
