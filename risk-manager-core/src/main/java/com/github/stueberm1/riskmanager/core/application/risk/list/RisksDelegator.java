package com.github.stueberm1.riskmanager.core.application.risk.list;

import static java.util.Objects.requireNonNull;

import com.github.stueberm1.riskmanager.core.model.risk.Risk;

import java.util.List;

public class RisksDelegator implements Risks {

    private final RiskLister lister;

    public RisksDelegator(RiskLister lister) {
        this.lister = requireNonNull(lister);
    }

    @Override
    public List<Risk> listAll() {
        return lister.listAll();
    }
}
