package com.github.stueberm1.riskmanager.core.in.risk.filter;

/// Defines an operation where all resulting risk **must exactly** meets the specified value.
/// @param <T> type of the attribute as well as of the search value
/// @param <R> The next step in the filter configuration
public interface AttributesEqualsOperation<T, R> {

    R isEqualTo(T value);
}
