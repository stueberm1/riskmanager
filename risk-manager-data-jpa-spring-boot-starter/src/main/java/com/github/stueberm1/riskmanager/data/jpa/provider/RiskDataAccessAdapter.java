package com.github.stueberm1.riskmanager.data.jpa.provider;

import com.github.stueberm1.riskmanager.core.out.persistence.RiskDao;
import com.github.stueberm1.riskmanager.core.out.persistence.RiskDataAccessService;
import com.github.stueberm1.riskmanager.data.jpa.risk.RiskDataRepository;
import com.github.stueberm1.riskmanager.types.risk.RiskIdentifier;

import java.util.List;
import java.util.Optional;

public class RiskDataAccessAdapter implements RiskDataAccessService {

    private final RiskDataRepository riskDataRepository;
    private final SimpleRiskDaoDataConverter  riskDataRiskDataConverter;

    public RiskDataAccessAdapter(RiskDataRepository riskDataRepository,
                                SimpleRiskDaoDataConverter riskDataRiskDataConverter) {
        this.riskDataRepository = riskDataRepository;
        this.riskDataRiskDataConverter = riskDataRiskDataConverter;
    }

    @Override
    public void save(RiskDao risk) {
        riskDataRepository.save(riskDataRiskDataConverter.convert(risk));
    }

    @Override
    public void delete(RiskIdentifier id) {
        riskDataRepository.deleteById(id.id());
    }

    @Override
    public Optional<RiskDao> find(RiskIdentifier id) {
        return riskDataRepository.findById(id.id()).map(riskDataRiskDataConverter::convert);
    }

    @Override
    public List<RiskDao> listAll() {
        return riskDataRepository.findAll().stream().map(riskDataRiskDataConverter::convert).toList();
    }
}
