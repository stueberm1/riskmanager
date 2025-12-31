package com.github.stueberm1.riskmanager.data.jpa.risk;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@AutoConfiguration
@ConditionalOnMissingBean({RiskDataRepository.class})
@EnableJpaRepositories(basePackageClasses = RiskDataRepository.class)
@EntityScan(basePackageClasses = RiskData.class)
public class RiskRepositoryAutoConfiguration {
}
