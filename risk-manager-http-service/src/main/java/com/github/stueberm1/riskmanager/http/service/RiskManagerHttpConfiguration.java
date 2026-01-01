package com.github.stueberm1.riskmanager.http.service;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;

import com.github.stueberm1.riskmanager.http.model.RiskIdentifierSerializer;
import com.github.stueberm1.riskmanager.http.model.SimpleNumericRiskIdentifierDeserializer;
import com.github.stueberm1.riskmanager.types.risk.RiskIdentifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class RiskManagerHttpConfiguration implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new NumericRiskIdentifierConverter());
    }

    @Bean
    public Module riskIdentifierModule() {
        SimpleModule module = new SimpleModule();
        module.addSerializer(RiskIdentifier.class, new RiskIdentifierSerializer());
        module.addDeserializer(RiskIdentifier.class, new SimpleNumericRiskIdentifierDeserializer());
        return module;
    }
}
