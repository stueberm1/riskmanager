package com.github.stueberm1.riskmanager.core.in.risk.filter;

/// Defines an operation, which is mostly useful on string attributes. The result  fits mostly the specification
/// of {@link String#contains(CharSequence)} even on other types
/// @param <T> type of the attribute as well as of the search snippet
/// @param <R> The next step in the filter configuration
public interface AttributeContainsOperation<T, R> {

    /// Each item of the result-set **must** have an attribute containing the sequence specified by the argument.
    ///
    /// @param snippet The sequence the client is looking for
    /// @return The next step in the configuration flow
    ///
    R contains(T snippet);
}
