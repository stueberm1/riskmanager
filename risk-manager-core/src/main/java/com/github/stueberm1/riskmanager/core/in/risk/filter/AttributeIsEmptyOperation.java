package com.github.stueberm1.riskmanager.core.in.risk.filter;

/// Contains an operation to check the current attribute of the risk is not set, yet.
///
/// @param <R> The next step in the filter configuration
public interface AttributeIsEmptyOperation<R> {

    /// The specified attribute of the risk **must** be unset (empty).
    /// @return the next step in the filter configuration
    R isEmpty();
}
