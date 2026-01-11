package com.github.stueberm1.riskmanager.config.core.application.list;

import com.github.stueberm1.riskmanager.config.core.application.RiskConverterAutoConfiguration;
import com.github.stueberm1.riskmanager.config.core.application.RiskServiceAutoConfiguration;
import com.github.stueberm1.riskmanager.core.application.risk.RiskConverter;
import com.github.stueberm1.riskmanager.core.application.risk.list.RiskLister;
import com.github.stueberm1.riskmanager.core.application.risk.list.RiskListerAdapter;
import com.github.stueberm1.riskmanager.core.application.risk.list.Risks;
import com.github.stueberm1.riskmanager.core.application.risk.list.RisksDelegator;
import com.github.stueberm1.riskmanager.core.domain.RiskFactory;
import com.github.stueberm1.riskmanager.core.out.persistence.RiskDataAccessService;
import com.github.stueberm1.riskmanager.core.out.persistence.RiskReportingService;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;


@AutoConfiguration(before = RiskServiceAutoConfiguration.class, after = RiskConverterAutoConfiguration.class)
@ConditionalOnMissingBean(Risks.class)
public class RisksAutoConfiguration {

    @Bean
    public Risks risks(RiskLister riskLister, RiskConverter riskConverter) {
        return new RisksDelegator(riskLister, riskConverter);
    }

    @Bean
    public RiskLister riskLister(RiskDataAccessService riskDataAccessService, RiskReportingService riskReportingService,
                                 RiskFactory riskFactory) {
         return new RiskListerAdapter(riskFactory, riskDataAccessService, riskReportingService);
     }
}
