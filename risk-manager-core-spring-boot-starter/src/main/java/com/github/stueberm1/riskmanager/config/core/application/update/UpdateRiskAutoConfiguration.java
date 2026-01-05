package com.github.stueberm1.riskmanager.config.core.application.update;

import com.github.stueberm1.riskmanager.config.core.application.RiskServiceAutoConfiguration;
import com.github.stueberm1.riskmanager.config.core.application.create.CreateRiskAutoConfiguration;
import com.github.stueberm1.riskmanager.core.application.risk.create.CreateRisk;
import com.github.stueberm1.riskmanager.core.application.risk.find.RiskFinder;
import com.github.stueberm1.riskmanager.core.application.risk.update.DefaultPatchRisk;
import com.github.stueberm1.riskmanager.core.application.risk.update.PatchRisk;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

@AutoConfiguration(after = CreateRiskAutoConfiguration.class, before = RiskServiceAutoConfiguration.class)
@ConditionalOnMissingBean(PatchRisk.class)
public class UpdateRiskAutoConfiguration {

    @Bean
    public PatchRisk patchRisk(CreateRisk createRiskPersistenceAdapter, RiskFinder riskFinder) {
        return new DefaultPatchRisk(createRiskPersistenceAdapter, riskFinder);
    }
}
