package com.github.stueberm1.riskmanager.config.core.domain;

import com.github.stueberm1.riskmanager.core.domain.RiskPatchFactory;
import com.github.stueberm1.riskmanager.core.domain.SimpleRiskPatchFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@ConditionalOnMissingBean(RiskPatchFactory.class)
public class RiskPatchFactoryAutoConfiguration {

    @Bean
    public RiskPatchFactory riskPatchFactory() {
        return new SimpleRiskPatchFactory();
    }

}
