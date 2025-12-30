package com.github.stueberm1.riskmanager.config.core.domain;

import com.github.stueberm1.riskmanager.config.core.application.RiskServiceAutoConfiguration;
import com.github.stueberm1.riskmanager.config.core.application.create.CreateRiskAutoConfiguration;
import com.github.stueberm1.riskmanager.config.core.application.find.RiskFinderAutoConfiguration;
import com.github.stueberm1.riskmanager.config.core.application.find.RiskReaderAutoConfiguration;
import com.github.stueberm1.riskmanager.config.core.application.list.RisksAutoConfiguration;
import com.github.stueberm1.riskmanager.core.domain.RiskFactory;
import com.github.stueberm1.riskmanager.core.domain.SimpleRiskFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

@AutoConfiguration(before = {CreateRiskAutoConfiguration.class, RiskReaderAutoConfiguration.class, RiskFinderAutoConfiguration.class,
CreateRiskAutoConfiguration.class, RisksAutoConfiguration.class, RiskServiceAutoConfiguration.class})
@ConditionalOnMissingBean(RiskFactory.class)
public class RiskFactoryAutoConfiguration {

    @Bean
    public RiskFactory riskFactory() {
        return new SimpleRiskFactory();
    }
}
