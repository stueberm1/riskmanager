package com.github.stueberm1.riskmanager.core.domain;

import static java.util.Objects.nonNull;

import com.github.stueberm1.riskmanager.core.in.risk.RiskTO;
import com.github.stueberm1.riskmanager.core.model.risk.*;
import com.github.stueberm1.riskmanager.core.out.persistence.RiskDao;

public class SimpleRiskFactory implements RiskFactory {

    @Override
    public Risk create(RiskTO risk) {
        SimpleRisk.Builder builder = SimpleRisk.builder()
                .hasId(risk.id())
                .withSeverity(risk.severity())
                .probabilityOfOccurrence(risk.probabilityOfOccurrence())
                .havingDescription(SimpleDescription.ofValue(risk.description()))
                .withDetailedInformation(SimpleDetails.ofValue(risk.details()));
                if (nonNull(risk.contingencyPlanning())) {
                    builder.contingencyPlanning(SimpleContingencyPlanningDescription.ofValue(risk.contingencyPlanning()));
                }
                if (nonNull(risk.mitigationStrategy())) {
                    builder.mitigationStrategy(SimpleMitigationStrategyDescription.ofValue(risk.mitigationStrategy()));
                }
        return builder.build();
    }

    @Override
    public Risk create(RiskDao risk) {
        SimpleRisk.Builder builder = SimpleRisk.builder()
                .hasId(risk.id())
                .withSeverity(risk.severity())
                .probabilityOfOccurrence(risk.probabilityOfOccurrence())
                .havingDescription(SimpleDescription.ofValue(risk.description()))
                .withDetailedInformation(SimpleDetails.ofValue(risk.details()));
        risk.contingencyPlanning()
                .map(SimpleContingencyPlanningDescription::ofValue)
                .ifPresent(builder::contingencyPlanning);
        risk.getMitigationStrategy()
                .map(SimpleMitigationStrategyDescription::ofValue)
                .ifPresent(builder::mitigationStrategy);
        return builder.build();
    }
}
