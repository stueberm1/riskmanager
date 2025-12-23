package com.github.stueberm1.riskmanager.types.risk;

import com.github.stueberm1.riskmanager.types.RiskManagerException;
import com.github.stueberm1.riskmanager.types.log.ErrorCode;

public abstract class IllegalRiskIdentifierException extends RiskManagerException {
    protected IllegalRiskIdentifierException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    protected IllegalRiskIdentifierException(String message, Throwable cause, ErrorCode errorCode) {
        super(message, cause, errorCode);
    }
}
