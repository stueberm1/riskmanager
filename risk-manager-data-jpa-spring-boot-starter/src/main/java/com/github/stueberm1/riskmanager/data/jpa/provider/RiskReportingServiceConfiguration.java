package com.github.stueberm1.riskmanager.data.jpa.provider;

import com.github.stueberm1.riskmanager.core.out.persistence.RiskReportingService;
import com.github.stueberm1.riskmanager.data.jpa.risk.RiskDataCriteriaSpecificationFactory;
import com.github.stueberm1.riskmanager.data.jpa.risk.RiskDataRepository;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@ConditionalOnMissingBean(RiskReportingService.class)
public class RiskReportingServiceConfiguration {

    @Bean
    public RiskReportingService riskReportingService(RiskDataCriteriaSpecificationFactory riskDataCriteriaSpecificationFactory,
        RiskDataRepository riskDataRepository,
        SimpleRiskDaoDataConverter simpleRiskDaoDataConverter) {
        return new RiskReportingAdapter(riskDataCriteriaSpecificationFactory, riskDataRepository, simpleRiskDaoDataConverter);
    }
}
