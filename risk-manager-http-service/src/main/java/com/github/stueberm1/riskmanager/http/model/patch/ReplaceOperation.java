package com.github.stueberm1.riskmanager.http.model.patch;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.github.stueberm1.riskmanager.http.model.JsonPointer;
import com.github.stueberm1.riskmanager.http.model.JsonPointerDeserializer;
import tools.jackson.databind.annotation.JsonDeserialize;

@JsonTypeName(ReplaceOperation.OPERATION_NAME)
public final class ReplaceOperation extends PathValueOperation {

    public static final String OPERATION_NAME = "replace";

    @JsonCreator
    public ReplaceOperation(@JsonDeserialize(using = JsonPointerDeserializer.class) @JsonProperty("path") JsonPointer path,
                            @JsonProperty("value") String value) {
        super(path, value);
    }

    @Override
    public String getOp() {
        return OPERATION_NAME;
    }
}
