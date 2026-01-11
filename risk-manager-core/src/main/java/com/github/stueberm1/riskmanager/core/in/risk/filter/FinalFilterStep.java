package com.github.stueberm1.riskmanager.core.in.risk.filter;

import com.github.stueberm1.riskmanager.core.in.risk.RiskTO;

import java.util.List;

/// This is the endpoint of a risk-filter configuration. It creates the final {@link FilterSpec} and performs the search.
public interface FinalFilterStep {

    /// Use the current {@link FilterSpec} and run the list operation. The result is a list of all risks meeting the
    /// configured filter settings.
    /// @return The filtered risks
    List<RiskTO> toList();
}
