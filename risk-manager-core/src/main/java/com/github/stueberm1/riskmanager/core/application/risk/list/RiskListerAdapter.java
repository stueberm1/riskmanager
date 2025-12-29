package com.github.stueberm1.riskmanager.core.application.risk.list;

import static java.util.Objects.requireNonNull;

import com.github.stueberm1.riskmanager.core.domain.RiskFactory;
import com.github.stueberm1.riskmanager.core.model.risk.Risk;
import com.github.stueberm1.riskmanager.core.out.persistence.RiskDataAccessService;

import java.util.List;

public class RiskListerAdapter implements RiskLister {

    private final RiskFactory riskFactory;

    private final RiskDataAccessService riskDataAccessService;

    public RiskListerAdapter(RiskFactory riskFactory, RiskDataAccessService riskDataAccessService) {
        this.riskFactory = requireNonNull(riskFactory, "riskFactory");
        this.riskDataAccessService = requireNonNull(riskDataAccessService, "riskDataAccessService");
    }

    @Override
    public List<Risk> listAll() {
        return riskDataAccessService.listAll().stream().map(riskFactory::create).toList();
    }
}
