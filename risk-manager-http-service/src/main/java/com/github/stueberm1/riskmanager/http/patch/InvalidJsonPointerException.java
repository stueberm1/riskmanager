package com.github.stueberm1.riskmanager.http.patch;

import com.github.stueberm1.riskmanager.http.model.JsonPointer;
import com.github.stueberm1.riskmanager.types.RiskManagerException;
import com.github.stueberm1.riskmanager.types.risk.RiskIdentifier;

public class InvalidJsonPointerException extends RiskManagerException {

    private final RiskIdentifier riskIdentifier;;
    private final JsonPointer jsonPointer;

    public InvalidJsonPointerException(RiskIdentifier riskIdentifier, JsonPointer jsonPointer) {
        super("json pointer points to a non-existent attribute");
        this.riskIdentifier = riskIdentifier;
        this.jsonPointer = jsonPointer;
    }

    public RiskIdentifier getRiskIdentifier() {
        return riskIdentifier;
    }

    public JsonPointer getJsonPointer() {
        return jsonPointer;
    }

}
