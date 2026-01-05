package com.github.stueberm1.riskmanager.core.domain;

import static java.util.Objects.nonNull;

import com.github.stueberm1.riskmanager.core.in.risk.RiskPatchTO;
import com.github.stueberm1.riskmanager.core.model.risk.*;

public class SimpleRiskPatchFactory implements RiskPatchFactory {

    @Override
    public RiskPatch create(RiskPatchTO riskPatch) {
        SimplePatch.Builder builder = SimplePatch
                .builder()
                .probabilityOfOccurrence(riskPatch.probabilityOfOccurrence())
                .withSeverity(riskPatch.severity());
        if(nonNull(riskPatch.details())) {
            builder.withDetailedInformation(SimpleDetails.ofValue(riskPatch.details()));
        }
        if (nonNull(riskPatch.contingencyPlanning())) {
            builder.contingencyPlanning(SimpleContingencyPlanningDescription.ofValue(riskPatch.contingencyPlanning()));
        }
        if (nonNull(riskPatch.mitigationStrategy())) {
            builder.mitigationStrategy(SimpleMitigationStrategyDescription.ofValue(riskPatch.mitigationStrategy()));
        }
        return builder.build();
    }
}
