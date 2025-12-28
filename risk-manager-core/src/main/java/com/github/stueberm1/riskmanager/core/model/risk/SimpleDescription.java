package com.github.stueberm1.riskmanager.core.model.risk;

public class SimpleDescription extends Description {

    private SimpleDescription(String content) {
        super(content);
    }

    public static SimpleDescription ofValue(String description) {
        return new SimpleDescription(description);
    }
}
