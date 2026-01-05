package com.github.stueberm1.riskmanager.http.model.patch;

import com.github.stueberm1.riskmanager.http.model.JsonPointer;
import com.github.stueberm1.riskmanager.types.RiskManagerException;

public class IllegalValueModificationRequestException extends RiskManagerException {

    private final JsonPointer path;

    public IllegalValueModificationRequestException(String message, JsonPointer path) {
        super(message);
        this.path = path;
    }

    public JsonPointer getPath() {
        return path;
    }
}
