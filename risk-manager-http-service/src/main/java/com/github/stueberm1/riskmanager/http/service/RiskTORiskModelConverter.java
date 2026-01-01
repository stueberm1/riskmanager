package com.github.stueberm1.riskmanager.http.service;

import com.github.stueberm1.riskmanager.core.in.risk.RiskTO;
import com.github.stueberm1.riskmanager.http.model.RiskJson;

public class RiskTORiskModelConverter implements RiskModelConverter {
    @Override
    public RiskJson convertToHttpModel(RiskTO riskTO) {
        RiskJson riskJson = new RiskJson();
        riskJson.setId(riskJson.getId());
        riskJson.setSeverity(riskTO.severity());
        riskJson.setProbabilityOfOccurrence(riskTO.probabilityOfOccurrence());
        riskJson.setDescription(riskTO.description());
        riskJson.setDetails(riskTO.details());
        riskJson.setContingencyPlanning(riskTO.contingencyPlanning());
        riskJson.setMitigationStrategy(riskTO.mitigationStrategy());
        return riskJson;
    }

    @Override
    public RiskTO convertToRiskModel(RiskJson riskJson) {
        return new RiskTO(riskJson.getId(), riskJson.getSeverity(), riskJson.getProbabilityOfOccurrence(),
                riskJson.getDescription(), riskJson.getDetails(), riskJson.getContingencyPlanning(),
                riskJson.getMitigationStrategy());
    }
}
