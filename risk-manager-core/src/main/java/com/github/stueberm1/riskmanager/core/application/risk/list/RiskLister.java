package com.github.stueberm1.riskmanager.core.application.risk.list;

import com.github.stueberm1.riskmanager.core.model.risk.Risk;

import java.util.List;

public interface RiskLister {

    List<Risk> listAll();
}
