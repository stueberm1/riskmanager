package com.github.stueberm1.riskmanager.http.patch;

import com.github.stueberm1.riskmanager.http.model.JsonPointer;

public abstract class PathValueOperation extends ValueSettingOperation {

    private final String value;

    public PathValueOperation(JsonPointer path, String value) {
        super(path);
        this.value = value;
    }


    @Override
    public String getValue() {
        return value;
    }


}
