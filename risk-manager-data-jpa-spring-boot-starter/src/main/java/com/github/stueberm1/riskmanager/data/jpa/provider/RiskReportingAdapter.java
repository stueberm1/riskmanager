package com.github.stueberm1.riskmanager.data.jpa.provider;

import com.github.stueberm1.riskmanager.core.out.persistence.RiskDao;
import com.github.stueberm1.riskmanager.core.out.persistence.RiskFilter;
import com.github.stueberm1.riskmanager.core.out.persistence.RiskReportingService;
import com.github.stueberm1.riskmanager.data.jpa.risk.RiskDataCriteriaSpecificationFactory;
import com.github.stueberm1.riskmanager.data.jpa.risk.RiskDataRepository;

import java.util.List;

public class RiskReportingAdapter implements RiskReportingService {

    private final RiskDataRepository riskDataRepository;
    private final SimpleRiskDaoDataConverter simpleRiskDaoDataConverter;
    private final RiskDataCriteriaSpecificationFactory riskDataCriteriaSpecificationFactory;

    public RiskReportingAdapter(RiskDataCriteriaSpecificationFactory riskDataCriteriaSpecificationFactory,
                                RiskDataRepository riskDataRepository,
                                SimpleRiskDaoDataConverter simpleRiskDaoDataConverter) {
        this.riskDataCriteriaSpecificationFactory = riskDataCriteriaSpecificationFactory;
        this.riskDataRepository = riskDataRepository;
        this.simpleRiskDaoDataConverter = simpleRiskDaoDataConverter;
    }

    @Override
    public List<RiskDao> listRisksFiltered(RiskFilter riskFilter) {
        return riskDataRepository.findAll(riskDataCriteriaSpecificationFactory.buildSpecification(riskFilter))
                .stream().map(simpleRiskDaoDataConverter::convert).toList();
    }
}
