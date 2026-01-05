package com.github.stueberm1.riskmanager.http.model;

import static java.util.Objects.nonNull;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ser.std.StdSerializer;


public class JsonPointerSerializer extends StdSerializer<JsonPointer> {
    public JsonPointerSerializer(Class<JsonPointer> t) {
        super(t);
    }

    public JsonPointerSerializer() {
        this(JsonPointer.class);
    }

    @Override
    public void serialize(JsonPointer value, tools.jackson.core.JsonGenerator gen, SerializationContext provider) throws JacksonException {
        if (nonNull(value)) {
            gen.writeString(value.getRawPath());
        }
    }

}
