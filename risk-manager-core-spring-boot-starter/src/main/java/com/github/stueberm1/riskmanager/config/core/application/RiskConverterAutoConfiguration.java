package com.github.stueberm1.riskmanager.config.core.application;

import com.github.stueberm1.riskmanager.core.application.risk.DefaultRiskConverter;
import com.github.stueberm1.riskmanager.core.application.risk.RiskConverter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

@AutoConfiguration(before = {RiskServiceAutoConfiguration.class})
@ConditionalOnMissingBean(RiskConverter.class)
public class RiskConverterAutoConfiguration {
    @Bean
    public RiskConverter riskConverter() {
        return new DefaultRiskConverter();
    }
}
