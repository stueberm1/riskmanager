package com.github.stueberm1.riskmanager.core.in.risk.filter;

public interface ContingencyPlanningFilterSpec extends IgnoreAttributeOperation<FilterMitigationStrategyStep>,
AttributeIsEmptyOperation<FilterMitigationStrategyStep>,
AttributeContainsOperation<String, FilterMitigationStrategyStep>{
}
