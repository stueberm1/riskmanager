package com.github.stueberm1.riskmanager.http.service;

import com.github.stueberm1.riskmanager.core.in.risk.RiskTO;
import com.github.stueberm1.riskmanager.http.model.RiskJson;

public interface RiskModelConverter {

    RiskJson convertToHttpModel(RiskTO riskTO);

    RiskTO convertToRiskModel(RiskJson riskJson);
}
