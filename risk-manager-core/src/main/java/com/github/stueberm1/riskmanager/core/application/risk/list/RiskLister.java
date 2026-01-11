package com.github.stueberm1.riskmanager.core.application.risk.list;

import com.github.stueberm1.riskmanager.core.model.risk.Risk;
import com.github.stueberm1.riskmanager.core.out.persistence.RiskFilter;

import java.util.List;

public interface RiskLister {

    List<Risk> listAll();

    List<Risk> listWithFilter(RiskFilter filter);
}
