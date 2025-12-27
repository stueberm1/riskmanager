package com.github.stueberm1.riskmanager.core.application.risk.find;

import static java.util.Objects.requireNonNull;

import com.github.stueberm1.riskmanager.core.domain.RiskFactory;
import com.github.stueberm1.riskmanager.core.model.risk.Risk;
import com.github.stueberm1.riskmanager.core.out.persistence.RiskDataAccessService;
import com.github.stueberm1.riskmanager.types.risk.RiskIdentifier;
import java.util.Optional;

public class RiskReaderAdapter implements RiskReader {

    private final RiskDataAccessService riskDataAccessService;
    private final RiskFactory riskFactory;

    public RiskReaderAdapter(RiskDataAccessService riskDataAccessService, RiskFactory riskFactory) {
        this.riskDataAccessService = requireNonNull(riskDataAccessService, "riskDataAccessService");
        this.riskFactory = requireNonNull(riskFactory, "riskFactory");
    }

    @Override
    public Optional<Risk> read(RiskIdentifier id) {
        return riskDataAccessService.read(id).map(riskFactory::create);
    }
}
