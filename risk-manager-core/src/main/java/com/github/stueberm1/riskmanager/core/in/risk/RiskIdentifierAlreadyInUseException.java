package com.github.stueberm1.riskmanager.core.in.risk;

import static java.util.Objects.requireNonNull;

import com.github.stueberm1.riskmanager.types.RiskManagerException;
import com.github.stueberm1.riskmanager.types.risk.RiskIdentifier;

public class RiskIdentifierAlreadyInUseException extends RiskManagerException {

    private final RiskIdentifier riskIdentifier;
    public RiskIdentifierAlreadyInUseException(String message, RiskIdentifier riskIdentifier) {
        super(message);
        this.riskIdentifier = requireNonNull(riskIdentifier, "riskIdentifier");
    }

    public RiskIdentifier getRiskIdentifier() {
        return riskIdentifier;
    }
}
