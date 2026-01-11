package com.github.stueberm1.riskmanager.data.jpa.provider;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

@AutoConfiguration(before = {RiskDataAccessAdapterConfiguration.class, RiskReportingServiceConfiguration.class})
@ConditionalOnMissingBean(SimpleRiskDaoDataConverter.class)
public class RiskDataConverterConfiguration {

    @Bean
    public SimpleRiskDaoDataConverter riskDataConverter() {
        return new SimpleRiskDaoConverter();
    }
}
