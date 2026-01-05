package com.github.stueberm1.riskmanager.http.model.patch;

import com.github.stueberm1.riskmanager.http.model.JsonPointer;
import com.github.stueberm1.riskmanager.types.RiskManagerException;

public class InvalidJsonPointerException extends RiskManagerException {

    private final JsonPointer jsonPointer;

    public InvalidJsonPointerException(JsonPointer jsonPointer) {
        super("json pointer points to a non-existent attribute");
        this.jsonPointer = jsonPointer;
    }

    public JsonPointer getJsonPointer() {
        return jsonPointer;
    }
}
