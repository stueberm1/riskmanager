package com.github.stueberm1.riskmanager.config.core.application.find;

import com.github.stueberm1.riskmanager.config.core.application.RiskServiceAutoConfiguration;
import com.github.stueberm1.riskmanager.core.application.risk.find.ReadRiskDelegator;
import com.github.stueberm1.riskmanager.core.application.risk.find.RiskFinder;
import com.github.stueberm1.riskmanager.core.application.risk.find.RiskReader;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

@AutoConfiguration(after = RiskFinderAutoConfiguration.class, before = RiskServiceAutoConfiguration.class)
@ConditionalOnMissingBean(RiskReader.class)
public class RiskReaderAutoConfiguration {

    @Bean
    public RiskReader riskReader(RiskFinder riskFinder) {
        return new ReadRiskDelegator(riskFinder);
    }
}
