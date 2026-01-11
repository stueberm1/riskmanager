package com.github.stueberm1.riskmanager.core.in.risk.filter;

/// The current attribute is not relevant for the search and will therefore not be considered.
/// @param <R> The next step in the filter configuration
public interface IgnoreAttributeOperation<R> {

    /// The current attribute is ignored in the search.
    /// @return The next step in the filter configuration
    R isIgnored();
}
