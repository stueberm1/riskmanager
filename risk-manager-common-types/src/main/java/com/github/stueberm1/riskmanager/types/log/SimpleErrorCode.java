package com.github.stueberm1.riskmanager.types.log;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Objects.requireNonNull;

public class SimpleErrorCode implements ErrorCode {

    private final String code;

    private SimpleErrorCode(String errorCode) {
        this.code = requireNonNull(errorCode);
        if (code.isBlank()) {
            throw new IllegalArgumentException(errorCode);
        }
    }

    private static final Map<String, SimpleErrorCode> FLYWEIGHT_POOL = new ConcurrentHashMap<>();

    public static ErrorCode of(String errorCode) {
        return FLYWEIGHT_POOL.computeIfAbsent(errorCode, SimpleErrorCode::new);
    }

    @Override
    public String code() {
        return code;
    }
}
