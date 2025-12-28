package com.github.stueberm1.riskmanager.core.application.risk.find;

import com.github.stueberm1.riskmanager.core.in.risk.RiskNotFoundException;
import com.github.stueberm1.riskmanager.core.model.risk.Risk;
import com.github.stueberm1.riskmanager.types.risk.RiskIdentifier;

import java.util.Optional;

import static java.util.Objects.requireNonNull;

/// The {@code ReadRiskDelegator} delegates the read-request to the backend {@link RiskFinder}.
/// Since the {@code RiskFinder} returns an {@link Optional#empty()} in case the Risk does not exist and
/// the {@link RiskReader}-interfaces promises that the {@link Risk} exists, it checks the result
public class ReadRiskDelegator implements RiskReader {

    private final RiskFinder riskFinder;

    public ReadRiskDelegator(RiskFinder riskFinder) {
        this.riskFinder = requireNonNull(riskFinder);
    }

    @Override
    public Risk read(final RiskIdentifier id) {
        Optional<Risk> risk = riskFinder.find(id);
        if (!risk.isEmpty()) {
            return  risk.get();
        }
        throw new RiskNotFoundException("Not found", id);
    }
}
