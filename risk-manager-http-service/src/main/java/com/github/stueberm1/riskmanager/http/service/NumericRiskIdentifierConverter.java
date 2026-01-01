package com.github.stueberm1.riskmanager.http.service;

import com.github.stueberm1.riskmanager.types.risk.RiskIdentifier;
import com.github.stueberm1.riskmanager.types.risk.SimpleNumericRiskIdentifier;
import org.springframework.core.convert.converter.Converter;


public class NumericRiskIdentifierConverter implements Converter<String, RiskIdentifier> {
    @Override
    public RiskIdentifier convert(String source) {
        return SimpleNumericRiskIdentifier.builder().withCurrentNumber(Long.parseLong(source)).build();
    }
}
