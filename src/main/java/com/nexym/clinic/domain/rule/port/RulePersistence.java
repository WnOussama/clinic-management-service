package com.nexym.clinic.domain.rule.port;

import com.nexym.clinic.domain.rule.model.Rule;

import java.util.Optional;

public interface RulePersistence {

    Optional<Rule> findGlobalRule();


}
