package com.github.stueberm1.riskmanager.core.in.risk.filter;

public interface MitigationStrategyFilterSpec extends IgnoreAttributeOperation<FinalFilterStep>,
AttributeIsEmptyOperation<FinalFilterStep>,
AttributeContainsOperation<String, FinalFilterStep> {
}
