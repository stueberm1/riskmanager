package com.github.stueberm1.riskmanager.data.jpa.provider;

import com.github.stueberm1.riskmanager.core.out.persistence.RiskDao;
import com.github.stueberm1.riskmanager.core.out.persistence.SimpleRiskDao;
import com.github.stueberm1.riskmanager.data.jpa.risk.RiskData;
import com.github.stueberm1.riskmanager.types.risk.RiskIdentifier;
import com.github.stueberm1.riskmanager.types.risk.SimpleNumericRiskIdentifier;

public class SimpleRiskDaoConverter implements SimpleRiskDaoDataConverter {

    @Override
    public RiskData convert(RiskDao riskDao) {
        RiskData riskData = new RiskData();
        riskData.setRiskIdentifier(riskDao.id().id());
        riskData.setSeverity(riskDao.severity());
        riskData.setProbabilityOfOccurrence(riskDao.probabilityOfOccurrence());
        riskData.setDescription(riskDao.description());
        riskData.setDetails(riskDao.details());
        riskDao.contingencyPlanning().ifPresent(riskData::setContingencyPlanning);
        riskDao.getMitigationStrategy().ifPresent(riskData::setMitigationStrategy);
        return riskData;
    }

    @Override
    public RiskDao convert(RiskData riskDao) {
        return SimpleRiskDao.builder()
                .hasId(convert(riskDao.getRiskIdentifier()))
                .withSeverity(riskDao.getSeverity())
                .probabilityOfOccurrence(riskDao.getProbabilityOfOccurrence())
                .havingDescription(riskDao.getDescription())
                .withDetailedInformation(riskDao.getDetails())
                .contingencyPlanning(riskDao.getContingencyPlanning())
                .mitigationStrategy(riskDao.getMitigationStrategy())
                .build();
    }

    private static RiskIdentifier convert(String riskIdentifier) {
        return SimpleNumericRiskIdentifier.builder()
                .withCurrentNumber(Long.parseLong(riskIdentifier))
                .build();
    }
}
