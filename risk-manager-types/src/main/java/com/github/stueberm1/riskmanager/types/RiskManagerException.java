package com.github.stueberm1.riskmanager.types;

import static java.util.Objects.requireNonNull;

import com.github.stueberm1.riskmanager.types.log.ErrorCode;

public abstract class  RiskManagerException extends RuntimeException {

    private final ErrorCode errorCode;

    public ErrorCode errorCode() {
        return errorCode;
    }

    protected RiskManagerException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    protected RiskManagerException(String message, Throwable cause, ErrorCode errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }
}
