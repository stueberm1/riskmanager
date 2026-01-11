package com.github.stueberm1.riskmanager.core.in.risk.filter;

import com.github.stueberm1.riskmanager.types.risk.ProbabilityOfOccurrence;

public interface ProbabilityOfOccurrenceFilterSpec extends IgnoreAttributeOperation<FilterDescriptionStep>,
        AttributesEqualsOperation<ProbabilityOfOccurrence, FilterDescriptionStep> {
}
