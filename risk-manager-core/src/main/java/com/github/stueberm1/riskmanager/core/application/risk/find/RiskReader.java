package com.github.stueberm1.riskmanager.core.application.risk.find;

import com.github.stueberm1.riskmanager.core.model.risk.Risk;
import com.github.stueberm1.riskmanager.types.risk.RiskIdentifier;

public interface RiskReader {
    Risk read(RiskIdentifier id);
}
