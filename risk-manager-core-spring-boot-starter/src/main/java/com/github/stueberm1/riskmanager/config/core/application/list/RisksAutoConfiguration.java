package com.github.stueberm1.riskmanager.config.core.application.list;

import com.github.stueberm1.riskmanager.config.core.application.RiskServiceAutoConfiguration;
import com.github.stueberm1.riskmanager.core.application.risk.list.RiskLister;
import com.github.stueberm1.riskmanager.core.application.risk.list.RiskListerAdapter;
import com.github.stueberm1.riskmanager.core.application.risk.list.Risks;
import com.github.stueberm1.riskmanager.core.application.risk.list.RisksDelegator;
import com.github.stueberm1.riskmanager.core.domain.RiskFactory;
import com.github.stueberm1.riskmanager.core.out.persistence.RiskDataAccessService;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;


@AutoConfiguration(before = RiskServiceAutoConfiguration.class)
@ConditionalOnMissingBean(Risks.class)
public class RisksAutoConfiguration {

    @Bean
    public Risks risks(RiskLister riskLister) {
        return new RisksDelegator(riskLister);
    }

    @Bean
    public RiskLister riskLister(RiskDataAccessService riskDataAccessService, RiskFactory riskFactory) {
         return new RiskListerAdapter(riskFactory, riskDataAccessService);
     }
}
