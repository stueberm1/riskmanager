package com.github.stueberm1.riskmanager.data.jpa.risk;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@AutoConfiguration
@ConditionalOnMissingBean({RiskDataRepository.class})
@EnableTransactionManagement
@EnableJpaRepositories(basePackageClasses = RiskDataRepository.class)
@EntityScan(basePackageClasses = RiskData.class)
public class RiskRepositoryAutoConfiguration {

    @Bean
    public RiskDataCriteriaSpecificationFactory riskDataCriteriaSpecificationFactory() {
        return new DefaultRiskDataCriteriaSpecificationFactory();
    }
}
