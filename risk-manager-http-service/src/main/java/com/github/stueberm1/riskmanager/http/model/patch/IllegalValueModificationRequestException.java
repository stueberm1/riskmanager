package com.github.stueberm1.riskmanager.http.model.patch;

import com.github.stueberm1.riskmanager.http.model.JsonPointer;
import com.github.stueberm1.riskmanager.types.RiskManagerException;
import com.github.stueberm1.riskmanager.types.risk.RiskIdentifier;

public class IllegalValueModificationRequestException extends RiskManagerException {

    private final JsonPointer path;
    private final RiskIdentifier riskIdentifier;

    public IllegalValueModificationRequestException(String message, JsonPointer path, RiskIdentifier riskIdentifier) {
        super(message);
        this.path = path;
        this.riskIdentifier = riskIdentifier;
    }

    public JsonPointer getPath() {
        return path;
    }

    public RiskIdentifier getRiskIdentifier() {
        return riskIdentifier;
    }
}
