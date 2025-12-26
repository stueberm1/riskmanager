package com.github.stueberm1.riskmanager.types;


public abstract class  RiskManagerException extends RuntimeException {


    protected RiskManagerException(String message) {
        super(message);
    }

    protected RiskManagerException(String message, Throwable cause) {
        super(message, cause);
    }
}
