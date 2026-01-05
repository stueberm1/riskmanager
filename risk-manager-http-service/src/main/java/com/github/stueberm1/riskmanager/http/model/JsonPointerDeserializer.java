package com.github.stueberm1.riskmanager.http.model;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.deser.std.StdDeserializer;

import static java.util.Objects.nonNull;

public class JsonPointerDeserializer extends StdDeserializer<JsonPointer> {

    public JsonPointerDeserializer(Class<?> vc) {
        super(vc);
    }

    public JsonPointerDeserializer() {
        this(JsonPointer.class);
    }

    @Override
    public JsonPointer deserialize(JsonParser p, DeserializationContext ctxt) throws JacksonException {
        String jsonPointer = p.getValueAsString();
        if (nonNull(jsonPointer)) {
            return new JsonPointer(jsonPointer);
        }
        return null;
    }

}
