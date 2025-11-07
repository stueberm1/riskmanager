package com.github.stueberm1.riskmanager.types.log;

public interface ErrorCode {

    String code();


    static ErrorCode getInstanceOf(String errorCode) {
        return SimpleErrorCode.of(errorCode);
    }
}
