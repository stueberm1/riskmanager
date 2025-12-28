package com.github.stueberm1.riskmanager.types.risk;

public class IllegalIdNumberException extends IllegalRiskIdentifierException {

    private final long illegalCurrentNumber;

    public static final String MESSAGE_CODE = "error-illegal-risk-identifier-number";

    public long getIllegalCurrentNumber() {
        return illegalCurrentNumber;
    }

    public IllegalIdNumberException(long illegalCurrentNumber) {
        super(MESSAGE_CODE);
        this.illegalCurrentNumber = illegalCurrentNumber;
    }

    public IllegalIdNumberException(Throwable cause, long illegalCurrentNumber) {
        super(MESSAGE_CODE, cause);
        this.illegalCurrentNumber = illegalCurrentNumber;
    }
}
