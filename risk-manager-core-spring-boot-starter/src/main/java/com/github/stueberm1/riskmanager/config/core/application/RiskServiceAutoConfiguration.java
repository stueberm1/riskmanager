package com.github.stueberm1.riskmanager.config.core.application;

import com.github.stueberm1.riskmanager.core.application.risk.ModelAdaptingRiskServiceFacade;
import com.github.stueberm1.riskmanager.core.application.risk.create.CreateRisk;
import com.github.stueberm1.riskmanager.core.application.risk.find.RiskReader;
import com.github.stueberm1.riskmanager.core.application.risk.list.Risks;
import com.github.stueberm1.riskmanager.core.domain.RiskFactory;
import com.github.stueberm1.riskmanager.core.in.risk.RiskService;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@ConditionalOnMissingBean(RiskService.class)
public class RiskServiceAutoConfiguration {

    @Bean
    public RiskService riskService(RiskFactory riskFactory, CreateRisk createRisk, RiskReader riskReader, Risks risks) {
        return ModelAdaptingRiskServiceFacade.builder()
                .riskFactory(riskFactory)
                .createRisk(createRisk)
                .riskReader(riskReader)
                .risks(risks)
                .build();
    }
}
