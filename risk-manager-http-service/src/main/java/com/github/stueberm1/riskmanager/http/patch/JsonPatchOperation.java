package com.github.stueberm1.riskmanager.http.patch;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.github.stueberm1.riskmanager.http.model.JsonPointer;
import com.github.stueberm1.riskmanager.http.model.JsonPointerSerializer;
import tools.jackson.databind.annotation.JsonSerialize;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "op"
)
@JsonSubTypes({@JsonSubTypes.Type(
        name = "add",
        value = AddOperation.class
), @JsonSubTypes.Type(
        name = "copy",
        value = CopyOperation.class
), @JsonSubTypes.Type(
        name = "move",
        value = MoveOperation.class
), @JsonSubTypes.Type(
        name = "remove",
        value = RemoveOperation.class
), @JsonSubTypes.Type(
        name = "replace",
        value = ReplaceOperation.class
), @JsonSubTypes.Type(
        name = "test",
        value = TestOperation.class
)})
@JsonIgnoreProperties(
        ignoreUnknown = true
)
public abstract class JsonPatchOperation  {

    protected final JsonPointer path;


    protected JsonPatchOperation(JsonPointer path) {
        this.path = path;
    }

    @JsonIgnore
    public abstract String getOp();

    @JsonSerialize(using = JsonPointerSerializer.class)
    public JsonPointer getPath() {
        return path;
    }

    public abstract void applyTo(RiskPatchBuilder builder);
}
