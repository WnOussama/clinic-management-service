package com.nexym.clinic.infra.rule.repository;

import com.nexym.clinic.domain.rule.model.Rule;
import com.nexym.clinic.domain.rule.port.RulePersistence;
import com.nexym.clinic.infra.rule.dao.RuleDao;
import com.nexym.clinic.infra.rule.mapper.RuleMapper;
import com.nexym.clinic.utils.FormatUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class RuleRepository implements RulePersistence {

    private final RuleDao ruleDao;
    private final RuleMapper ruleMapper;

    @Override
    public Optional<Rule> findGlobalRule() {
        var rules = ruleDao.findAll();
        if (FormatUtil.isFilled(rules)) {
            var optionalGlobalRule = rules.stream().findFirst();
            return optionalGlobalRule.map(ruleMapper::map);
        }
        return Optional.empty();
    }
}
