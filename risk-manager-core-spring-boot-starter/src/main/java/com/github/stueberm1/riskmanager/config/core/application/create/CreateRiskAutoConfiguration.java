package com.github.stueberm1.riskmanager.config.core.application.create;

import com.github.stueberm1.riskmanager.config.core.application.RiskServiceAutoConfiguration;
import com.github.stueberm1.riskmanager.config.core.application.find.RiskFinderAutoConfiguration;
import com.github.stueberm1.riskmanager.config.core.application.find.RiskReaderAutoConfiguration;
import com.github.stueberm1.riskmanager.core.application.risk.create.CreateRisk;
import com.github.stueberm1.riskmanager.core.application.risk.create.CreateRiskPersistenceAdapter;
import com.github.stueberm1.riskmanager.core.application.risk.create.IdValidatingCreateRisk;
import com.github.stueberm1.riskmanager.core.application.risk.find.RiskFinder;
import com.github.stueberm1.riskmanager.core.application.risk.find.RiskReader;
import com.github.stueberm1.riskmanager.core.out.persistence.RiskDataAccessService;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

@AutoConfiguration(after = RiskFinderAutoConfiguration.class, before = {RiskServiceAutoConfiguration.class, RiskReaderAutoConfiguration.class})
@ConditionalOnMissingBean(CreateRisk.class)
public class CreateRiskAutoConfiguration {


    @Bean
    public CreateRisk createRisk(CreateRisk createRiskPersistenceAdapter, RiskFinder riskFinder) {
        return new IdValidatingCreateRisk(createRiskPersistenceAdapter, riskFinder);
    }

    @Bean
    @ConditionalOnMissingBean(name = {"createRiskPersistenceAdapter"})
    public CreateRisk createRiskPersistenceAdapter(RiskDataAccessService riskDataAccessService) {
        return new CreateRiskPersistenceAdapter(riskDataAccessService);
    }
}
