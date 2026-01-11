package com.github.stueberm1.riskmanager.core.application.risk;

import com.github.stueberm1.riskmanager.core.in.risk.RiskTO;
import com.github.stueberm1.riskmanager.core.model.risk.Risk;

public interface RiskConverter {
    RiskTO convert(Risk risk);
}
