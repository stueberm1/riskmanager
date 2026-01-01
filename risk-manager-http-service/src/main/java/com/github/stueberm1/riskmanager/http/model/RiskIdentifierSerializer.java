package com.github.stueberm1.riskmanager.http.model;

import static java.util.Objects.nonNull;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.github.stueberm1.riskmanager.types.risk.RiskIdentifier;

import java.io.IOException;

public class RiskIdentifierSerializer extends StdSerializer<RiskIdentifier> {

    public RiskIdentifierSerializer() {
        this(null);
    }

    public RiskIdentifierSerializer(Class<RiskIdentifier> t) {
        super(t);
    }

    @Override
    public void serialize(RiskIdentifier riskIdentifier, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (nonNull(riskIdentifier)) {
            jsonGenerator.writeString(riskIdentifier.id());
        }
    }
}
