package com.github.stueberm1.riskmanager.http.model.patch;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.github.stueberm1.riskmanager.http.patch.RiskPatchBuilder;

/// Type mapping of a Json-Patch structure as defined in [RFC 6902](https://datatracker.ietf.org/doc/html/rfc6902)
public final class JsonPatch {

    private final JsonPatchOperation[] operations;

    @JsonCreator
    public JsonPatch(JsonPatchOperation[] operations) {
        this.operations = operations;
    }

    public JsonPatchOperation[] getOperations() {
        return operations;
    }

    public void assignTo(RiskPatchBuilder builder) {
        for (JsonPatchOperation operation : operations) {
            operation.applyTo(builder);
        }
    }

}
