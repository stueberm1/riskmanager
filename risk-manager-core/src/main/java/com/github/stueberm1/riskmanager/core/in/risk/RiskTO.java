package com.github.stueberm1.riskmanager.core.in.risk;

import com.github.stueberm1.riskmanager.types.risk.ProbabilityOfOccurrence;
import com.github.stueberm1.riskmanager.types.risk.RiskIdentifier;
import com.github.stueberm1.riskmanager.types.risk.Severity;

public record RiskTO(RiskIdentifier id, Severity severity, ProbabilityOfOccurrence probabilityOfOccurrence,
                     String description, String details, String contingencyPlanning,
                     String mitigationStrategy) {
}
