package com.github.stueberm1.riskmanager.data.jpa.provider;

import com.github.stueberm1.riskmanager.core.out.persistence.RiskDataAccessService;
import com.github.stueberm1.riskmanager.data.jpa.risk.RiskDataRepository;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@ConditionalOnMissingBean(RiskDataAccessService.class)
public class RiskDataAccessAdapterConfiguration {

    @Bean
    public RiskDataAccessService riskDataAccessService(RiskDataRepository riskDataRepository,
                                                       SimpleRiskDaoDataConverter riskDataConverter) {
        return new RiskDataAccessAdapter(riskDataRepository, riskDataConverter);
    }
}
