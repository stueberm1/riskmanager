package com.github.stueberm1.riskmanager.types.risk;

import com.github.stueberm1.riskmanager.types.RiskManagerException;;

public abstract class IllegalRiskIdentifierException extends RiskManagerException {
    protected IllegalRiskIdentifierException(String message) {
        super(message);
    }

    protected IllegalRiskIdentifierException(String message, Throwable cause) {
        super(message, cause);
    }
}
