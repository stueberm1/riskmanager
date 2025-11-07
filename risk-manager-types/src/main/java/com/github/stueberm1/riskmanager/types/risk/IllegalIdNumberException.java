package com.github.stueberm1.riskmanager.types.risk;

import com.github.stueberm1.riskmanager.types.log.ErrorCode;

public class IllegalIdNumberException extends IllegalRiskIdentifierException {

    private final long illegalCurrentNumber;

    public static final String MESSAGE_CODE = "error-illegal-risk-identifier-number";
    public static final ErrorCode ERROR_CODE = ErrorCode.getInstanceOf("RISK-0001");

    public long getIllegalCurrentNumber() {
        return illegalCurrentNumber;
    }

    public IllegalIdNumberException(long illegalCurrentNumber) {
        super(MESSAGE_CODE, ERROR_CODE);
        this.illegalCurrentNumber = illegalCurrentNumber;
    }

    public IllegalIdNumberException(Throwable cause, long illegalCurrentNumber) {
        super(MESSAGE_CODE, cause, ERROR_CODE);
        this.illegalCurrentNumber = illegalCurrentNumber;
    }
}
