package com.github.stueberm1.riskmanager.core.application.risk.list;

import static java.util.Objects.requireNonNull;

import com.github.stueberm1.riskmanager.core.application.risk.RiskConverter;
import com.github.stueberm1.riskmanager.core.in.risk.filter.FilterSpec;
import com.github.stueberm1.riskmanager.core.model.risk.Risk;

import java.util.List;

public class RisksDelegator implements Risks {

    private final RiskLister lister;
    private final RiskConverter riskConverter;

    public RisksDelegator(RiskLister lister, RiskConverter riskConverter) {
        this.lister = requireNonNull(lister, "risk lister must not be null");
        this.riskConverter = requireNonNull(riskConverter,  "risk converter must not be null");
    }

    @Override
    public List<Risk> listAll() {
        return lister.listAll();
    }

    @Override
    public FilterSpec listFilteredWith() {
        return new RiskFilterBuilder(lister, riskConverter);
    }
}
