package com.github.stueberm1.riskmanager.core.in.risk;

import com.github.stueberm1.riskmanager.types.RiskManagerException;
import com.github.stueberm1.riskmanager.types.risk.RiskIdentifier;

import static java.util.Objects.requireNonNull;

public class RiskNotFoundException extends RiskManagerException {

    private final RiskIdentifier riskIdentifier;
    public RiskNotFoundException(String message, RiskIdentifier riskIdentifier) {
        super(message);
        this.riskIdentifier = requireNonNull(riskIdentifier, "riskIdentifier");
    }

    public RiskIdentifier getRiskIdentifier() {
        return riskIdentifier;
    }
}
