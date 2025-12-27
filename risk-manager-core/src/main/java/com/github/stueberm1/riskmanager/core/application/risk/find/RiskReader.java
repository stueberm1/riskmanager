package com.github.stueberm1.riskmanager.core.application.risk.find;

import com.github.stueberm1.riskmanager.core.model.risk.Risk;
import com.github.stueberm1.riskmanager.types.risk.RiskIdentifier;

import java.util.Optional;

public interface RiskReader {
    Optional<Risk> read(RiskIdentifier id);
}
