package com.github.stueberm1.riskmanager.config.core.application.find;

import com.github.stueberm1.riskmanager.core.application.risk.find.RiskFinder;
import com.github.stueberm1.riskmanager.core.application.risk.find.RiskFinderAdapter;
import com.github.stueberm1.riskmanager.core.domain.RiskFactory;
import com.github.stueberm1.riskmanager.core.out.persistence.RiskDataAccessService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnMissingBean(RiskFinder.class)
public class RiskFinderAutoConfiguration {

    @Bean
    public RiskFinder riskFinder(RiskDataAccessService riskDataAccessService, RiskFactory riskFactory) {
        return new RiskFinderAdapter(riskDataAccessService, riskFactory);
    }
}
