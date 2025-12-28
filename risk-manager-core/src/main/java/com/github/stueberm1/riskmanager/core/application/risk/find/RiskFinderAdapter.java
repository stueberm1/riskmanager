package com.github.stueberm1.riskmanager.core.application.risk.find;

import com.github.stueberm1.riskmanager.core.domain.RiskFactory;
import com.github.stueberm1.riskmanager.core.model.risk.Risk;
import com.github.stueberm1.riskmanager.core.out.persistence.RiskDataAccessService;
import com.github.stueberm1.riskmanager.types.risk.RiskIdentifier;

import java.util.Optional;

import static java.util.Objects.requireNonNull;

public class RiskFinderAdapter implements RiskFinder {

    private final RiskDataAccessService riskDataAccessService;
    private final RiskFactory riskFactory;

    public RiskFinderAdapter(RiskDataAccessService riskDataAccessService, RiskFactory riskFactory) {
        this.riskDataAccessService = requireNonNull(riskDataAccessService, "riskDataAccessService");
        this.riskFactory = requireNonNull(riskFactory, "riskFactory");
    }

    @Override
    public Optional<Risk> find(RiskIdentifier id) {
        return riskDataAccessService.find(id).map(riskFactory::create);
    }
}
