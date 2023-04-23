package com.nexym.clinic.infra.rule.mapper;

import com.nexym.clinic.domain.rule.model.Rule;
import com.nexym.clinic.infra.rule.entity.RuleEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface RuleMapper {

    Rule map(RuleEntity ruleEntity);
}
