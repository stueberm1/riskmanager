package com.github.stueberm1.riskmanager.http.model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.github.stueberm1.riskmanager.types.risk.RiskIdentifier;
import com.github.stueberm1.riskmanager.types.risk.SimpleNumericRiskIdentifier;

import java.io.IOException;

public class SimpleNumericRiskIdentifierDeserializer extends StdDeserializer<RiskIdentifier> {

    public SimpleNumericRiskIdentifierDeserializer() {
        this(null);
    }

    public SimpleNumericRiskIdentifierDeserializer(Class<RiskIdentifier> valueType) {
        super(valueType);
    }

    @Override
    public RiskIdentifier deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {
        JsonToken  currentToken = jsonParser.getCurrentToken();
        if (currentToken == JsonToken.VALUE_NUMBER_INT) {
            return ofNumericValue(jsonParser.getLongValue());
        } else if (currentToken == JsonToken.VALUE_STRING) {
            return ofStringValue(jsonParser.getText());
        }
        return null;
    }

    private static RiskIdentifier ofStringValue(String idValue) {
        return ofNumericValue(Long.parseLong(idValue));
    }

    private static RiskIdentifier ofNumericValue(Long idValue) {
        return SimpleNumericRiskIdentifier.builder().withCurrentNumber(idValue).build();
    }
}
