package com.github.stueberm1.riskmanager.core.in.risk;

import com.github.stueberm1.riskmanager.core.in.risk.filter.AttributesEqualsOperation;
import com.github.stueberm1.riskmanager.core.in.risk.filter.FilterProbabilityOfOccurrenceStep;
import com.github.stueberm1.riskmanager.core.in.risk.filter.IgnoreAttributeOperation;
import com.github.stueberm1.riskmanager.types.risk.Severity;

public interface SeverityFilterSpec extends IgnoreAttributeOperation<FilterProbabilityOfOccurrenceStep>,
        AttributesEqualsOperation<Severity, FilterProbabilityOfOccurrenceStep> {
}