package com.github.stueberm1.riskmanager.core.domain;

import com.github.stueberm1.riskmanager.core.in.risk.RiskTO;
import com.github.stueberm1.riskmanager.core.model.risk.Risk;
import com.github.stueberm1.riskmanager.core.out.persistence.RiskDao;

public interface RiskFactory {
    Risk create(RiskTO risk);
    Risk create(RiskDao risk);
}
