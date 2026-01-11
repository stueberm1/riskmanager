package com.github.stueberm1.riskmanager.core.in.risk.filter;

import com.github.stueberm1.riskmanager.core.in.risk.RiskService;
import com.github.stueberm1.riskmanager.types.RiskManagerException;

/// The {@code InvalidFilterConfigurationException} is thrown by {@link FinalFilterStep#toList()} if all the
/// attribute filter steps used {@link IgnoreAttributeOperation#isIgnored()}.
/// The filtered list-operation needs at least one valid filter. For unfiltered risk-lists the system provides
/// {@link RiskService#listAll()}
public class InvalidFilterConfigurationException extends RiskManagerException {

    public InvalidFilterConfigurationException(String message) {
        super(message);
    }
}
