package com.nexym.clinic.infra.rule.dao;

import com.nexym.clinic.infra.rule.entity.RuleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RuleDao extends JpaRepository<RuleEntity, Long> {

}
