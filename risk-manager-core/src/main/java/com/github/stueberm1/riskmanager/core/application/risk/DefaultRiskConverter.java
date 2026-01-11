package com.github.stueberm1.riskmanager.core.application.risk;

import com.github.stueberm1.riskmanager.core.in.risk.RiskTO;
import com.github.stueberm1.riskmanager.core.model.risk.ContingencyPlanning;
import com.github.stueberm1.riskmanager.core.model.risk.MitigationStrategy;
import com.github.stueberm1.riskmanager.core.model.risk.Risk;

public class DefaultRiskConverter implements RiskConverter{

    @Override
    public RiskTO convert(Risk risk) {
        return new RiskTO(risk.id(), risk.severity(), risk.probabilityOfOccurrence(), risk.description().value(),
                risk.details().detailContent(), risk.contingencyPlanning().map(ContingencyPlanning::plan).orElse(null),
                risk.getMitigationStrategy().map(MitigationStrategy::strategy).orElse(null));
    }
}
