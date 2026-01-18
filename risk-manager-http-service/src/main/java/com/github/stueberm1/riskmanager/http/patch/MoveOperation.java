package com.github.stueberm1.riskmanager.http.patch;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.github.stueberm1.riskmanager.http.model.JsonPointer;
import com.github.stueberm1.riskmanager.http.model.JsonPointerDeserializer;
import tools.jackson.databind.annotation.JsonDeserialize;

@JsonTypeName(MoveOperation.OPERATION_NAME)
public final class MoveOperation  extends DualPathOperation {

    public static final String OPERATION_NAME = "move";

    @JsonCreator
    public MoveOperation(@JsonDeserialize(using = JsonPointerDeserializer.class) @JsonProperty("path") JsonPointer path,
                         @JsonProperty("from") JsonPointer from) {
        super(path, from);
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
