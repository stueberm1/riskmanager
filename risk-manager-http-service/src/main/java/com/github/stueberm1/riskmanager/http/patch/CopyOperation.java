package com.github.stueberm1.riskmanager.http.patch;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.github.stueberm1.riskmanager.http.model.JsonPointer;
import com.github.stueberm1.riskmanager.http.model.JsonPointerDeserializer;
import tools.jackson.databind.annotation.JsonDeserialize;

@JsonTypeName(CopyOperation.OPERATION_NAME)
public final class CopyOperation extends DualPathOperation {

    public static final String OPERATION_NAME = "copy";

    @JsonCreator
    public CopyOperation(@JsonDeserialize(using = JsonPointerDeserializer.class) @JsonProperty("path") JsonPointer path,
                         @JsonDeserialize(using = JsonPointerDeserializer.class) @JsonProperty("from") JsonPointer from) {
        super(path, from);
    }

    @Override
    public String getOp() {
        return OPERATION_NAME;
    }
}
