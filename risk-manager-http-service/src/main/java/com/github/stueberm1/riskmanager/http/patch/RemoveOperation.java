package com.github.stueberm1.riskmanager.http.patch;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.github.stueberm1.riskmanager.http.model.JsonPointer;
import com.github.stueberm1.riskmanager.http.model.JsonPointerDeserializer;
import tools.jackson.databind.annotation.JsonDeserialize;

@JsonTypeName(RemoveOperation.OPERATION_NAME)
public final class RemoveOperation extends JsonPatchOperation {

    public static final String OPERATION_NAME = "remove";

    @JsonCreator
    public RemoveOperation(@JsonDeserialize(using = JsonPointerDeserializer.class) @JsonProperty("path") JsonPointer path) {
        super(path);
    }

    @Override
    public void applyTo(RiskPatchBuilder builder) {
        throw new UnsupportedJsonPatchOperationException(
                "Business constraints does not allow to remove an attribute value",
                this);
    }

    @Override
    public String getOp() {
        return OPERATION_NAME;
    }
}
