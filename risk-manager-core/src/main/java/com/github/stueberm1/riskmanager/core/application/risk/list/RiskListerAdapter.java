package com.github.stueberm1.riskmanager.core.application.risk.list;

import static java.util.Objects.requireNonNull;

import com.github.stueberm1.riskmanager.core.domain.RiskFactory;
import com.github.stueberm1.riskmanager.core.model.risk.Risk;
import com.github.stueberm1.riskmanager.core.out.persistence.RiskDataAccessService;
import com.github.stueberm1.riskmanager.core.out.persistence.RiskFilter;
import com.github.stueberm1.riskmanager.core.out.persistence.RiskReportingService;

import java.util.List;

public class RiskListerAdapter implements RiskLister {

    private final RiskFactory riskFactory;

    private final RiskDataAccessService riskDataAccessService;

    private final RiskReportingService riskReportingService;

    public RiskListerAdapter(RiskFactory riskFactory, RiskDataAccessService riskDataAccessService, RiskReportingService riskReportingService) {
        this.riskFactory = requireNonNull(riskFactory, "riskFactory");
        this.riskDataAccessService = requireNonNull(riskDataAccessService, "riskDataAccessService");
        this.riskReportingService = requireNonNull(riskReportingService, "riskReportingService");
    }

    @Override
    public List<Risk> listAll() {
        return riskDataAccessService.listAll().stream().map(riskFactory::create).toList();
    }

    @Override
    public List<Risk> listWithFilter(RiskFilter filter) {
        return riskReportingService.listRisksFiltered(filter).stream().map(riskFactory::create).toList();
    }

}
