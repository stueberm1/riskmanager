package com.github.stueberm1.riskmanager.http.patch;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.github.stueberm1.riskmanager.http.model.JsonPointer;
import com.github.stueberm1.riskmanager.http.model.JsonPointerDeserializer;
import tools.jackson.databind.annotation.JsonDeserialize;

@JsonTypeName(AddOperation.OPERATION_NAME)
public final class AddOperation extends PathValueOperation {

    public static final String OPERATION_NAME = "add";

    @JsonCreator
    public AddOperation(@JsonDeserialize(using = JsonPointerDeserializer.class) @JsonProperty("path") JsonPointer path,
                        @JsonProperty("value") String value) {
        super(path, value);
    }

    @Override
    public String getOp() {
        return OPERATION_NAME;
    }
}
